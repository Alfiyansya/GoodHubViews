package com.alfiansyah.goodhubviews.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailGithubUserViewModel @Inject constructor(val githubUserUseCase: GithubUserUseCase): ViewModel(){
    private val _detailGithubUser = MutableLiveData<Resource<GithubUser>>()
    val detailGithubUser: LiveData<Resource<GithubUser>> = _detailGithubUser
    fun getDetailGithubUser(username: String) {
        Log.i("DetailGithubUserViewModel", "getDetailGithubUser: ")
        githubUserUseCase.getDetailGithubUser(username = username)
            .asLiveData<Resource<GithubUser>>()
            .observeForever { resource ->
                Log.i("DetailGithubUserViewModel", "getDetailGithubUser: $resource")
                _detailGithubUser.value = resource
            }
    }
}