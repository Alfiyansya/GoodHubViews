package com.alfiansyah.goodhubviews.core.data.source.remote.network

import com.alfiansyah.goodhubviews.core.BuildConfig.API_KEY
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserDetailResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserItemResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token $API_KEY", "UserResponse-Agent: request")
    suspend fun getListUser(): List<GithubUserItemResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $API_KEY", "UserResponse-Agent: request")
    suspend fun getDetailUser(@Path("username") username: String): GithubUserDetailResponse


    @GET("search/users")
    @Headers("Authorization: token $API_KEY", "UserResponse-Agent: request")
    suspend fun getUserBySearch(@Query("q") query: String): GithubUserSearchResponse
}