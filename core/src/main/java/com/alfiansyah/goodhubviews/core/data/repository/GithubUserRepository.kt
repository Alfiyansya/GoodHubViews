package com.alfiansyah.goodhubviews.core.data.repository

import com.alfiansyah.goodhubviews.core.data.NetworkBoundResource
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.data.source.local.LocalDataSource
import com.alfiansyah.goodhubviews.core.data.source.remote.RemoteDataSource
import com.alfiansyah.goodhubviews.core.data.source.remote.network.ApiResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserDetailResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserItemResponse
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.domain.repository.IGithubUserRepository
import com.alfiansyah.goodhubviews.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IGithubUserRepository {
    override fun getAllGithubUser(): Flow<Resource<List<GithubUser>>> =
        object : NetworkBoundResource<List<GithubUser>, List<GithubUserItemResponse>>() {
            override fun loadFromDB(): Flow<List<GithubUser>> {
                return localDataSource.getAllGithubUser().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<GithubUser>): Boolean =
                data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<GithubUserItemResponse>>> =
                remoteDataSource.getAllUser()

            override suspend fun saveCallResult(data: List<GithubUserItemResponse>) {
                val githubUserList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertGithubUserList(githubUserList)
            }

        }.asFlow()

    override fun getDetailGithubUser(username: String): Flow<Resource<GithubUser>> =
        object : NetworkBoundResource<GithubUser, GithubUserDetailResponse>() {
            override fun loadFromDB(): Flow<GithubUser> {
                return localDataSource.getDetailGithubUser(username).map {entity ->
                    if (entity != null) {
                        DataMapper.mapEntitiesToDomain(entity)
                    } else {
                        // Return a minimal GithubUser object when no local data exists
                        GithubUser(
                            login = username,
                            id = 0,
                            avatarUrl = null,
                            name = null,
                            company = null,
                            location = null,
                            publicRepos = 0,
                            followers = 0,
                            following = 0
                        )
                    }
                }
            }

            override fun shouldFetch(data: GithubUser): Boolean =
                data.name == null

            override suspend fun createCall(): Flow<ApiResponse<GithubUserDetailResponse>> =
                remoteDataSource.getDetailUser(username)

            override suspend fun saveCallResult(data: GithubUserDetailResponse) {
                val githubUser = DataMapper.mapResponsesToEntities(data)
                localDataSource.upsertGithubUser(githubUser)
            }

        }.asFlow()

    override fun getUserBySearch(query: String): Flow<Resource<List<GithubUser>>> {

        return flow{
            emit(Resource.Loading())
            remoteDataSource.getUserBySearch(query).map {
                when (it) {
                    is ApiResponse.Success -> {
                        val domainData = DataMapper.mapSearchResponseToDomain(it.data)
                        Resource.Success(domainData)
                    }

                    is ApiResponse.Empty -> {
                        Resource.Success(emptyList())
                    }

                    is ApiResponse.Error -> {
                        Resource.Error(it.message)
                    }
                }
            }.flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }
}