package com.alfiansyah.goodhubviews.core.domain.usecase

import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserUseCase {
    fun getAllGithubUser(): Flow<Resource<List<GithubUser>>>
    fun getDetailGithubUser(username: String): Flow<Resource<GithubUser>>
    fun getSearchGithubUser(query: String): Flow<Resource<List<GithubUser>>>
//    fun getGithubUserDetail(username: String)
//    fun getGithubUserFollower(username: String)
//    fun getGithubUserFollowing(username: String)
//    fun getSearchGithubUser(username: String)
}