package com.alfiansyah.goodhubviews.core.data.source.remote

import android.util.Log
import com.alfiansyah.goodhubviews.core.data.source.remote.network.ApiResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.network.ApiService
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserDetailResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserItemResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
     fun getAllUser(): Flow<ApiResponse<List<GithubUserItemResponse>>> {
        return flow{
            try {
                val response = apiService.getListUser()
                if (response.isNotEmpty()){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource Error: ", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
    fun getDetailUser(username: String): Flow<ApiResponse<GithubUserDetailResponse>>{
        return flow {
            try {
                val response = apiService.getDetailUser(username)
                if (response.name?.isNotEmpty() == true){
                    emit(ApiResponse.Success(response))
                    Log.d("RemoteDataSource", "Success: $response")
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource Error: ", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
    fun getUserBySearch(query: String): Flow<ApiResponse<GithubUserSearchResponse>>{
        return flow{
            try {
                val response = apiService.getUserBySearch(query)
                if (response.userItems?.isNotEmpty() == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource Error: ", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }


}