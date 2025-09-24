package com.alfiansyah.goodhubviews.core.data.source.local

import com.alfiansyah.goodhubviews.core.data.source.local.entity.GithubUserEntity
import com.alfiansyah.goodhubviews.core.data.source.local.room.GithubUserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val githubUserDao: GithubUserDao){
    fun getAllGithubUser() : Flow<List<GithubUserEntity>> = githubUserDao.getAllUser()
    fun getDetailGithubUser(username: String) : Flow<GithubUserEntity> = githubUserDao.getDetailGithubUser(username)
    suspend fun insertGithubUserList(githubUserList: List<GithubUserEntity>) = githubUserDao.insertGithubUserList(githubUserList)
    suspend fun upsertGithubUser(githubUser: GithubUserEntity) = githubUserDao.upsertGithubUser(githubUser)
}