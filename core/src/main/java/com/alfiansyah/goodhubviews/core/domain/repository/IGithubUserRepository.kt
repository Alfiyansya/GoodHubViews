package com.alfiansyah.goodhubviews.core.domain.repository

import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface IGithubUserRepository {
     fun getAllGithubUser(): Flow<Resource<List<GithubUser>>>
     fun getDetailGithubUser(username: String): Flow<Resource<GithubUser>>
     fun getUserBySearch(query: String): Flow<Resource<List<GithubUser>>>
}