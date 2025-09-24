package com.alfiansyah.goodhubviews.profile

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
class SearchViewModel @Inject constructor(val githubUserUseCase: GithubUserUseCase) : ViewModel() {
    private val _userBySearch = MutableLiveData<Resource<List<GithubUser>>>()
    val userBySearch: LiveData<Resource<List<GithubUser>>> = _userBySearch
    fun getUserBySearch(query: String) {
        Log.i("SearchGithubUserViewModel", "getSearchGithubUser: ")
        githubUserUseCase.getSearchGithubUser(query = query)
            .asLiveData()
            .observeForever { resource ->
                Log.i("SearchGithubUserViewModel", "getSearchGithubUser: $resource")
                _userBySearch.value = resource
            }
    }
}