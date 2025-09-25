package com.alfiansyah.goodhubviews.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.goodhubviews.core.data.Resource
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser
import com.alfiansyah.goodhubviews.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUserUseCase: GithubUserUseCase) : ViewModel(){
    private val _githubUserState = MutableStateFlow<Resource<List<GithubUser>>>(Resource.Loading())
    val githubUserState : StateFlow<Resource<List<GithubUser>>> = _githubUserState.asStateFlow()

    init {
        fetchGithubUsers()
    }

    fun refreshGithubUsers() {
        fetchGithubUsers()
    }
    private fun fetchGithubUsers() {
        _githubUserState.value = Resource.Loading()
        viewModelScope.launch {
            githubUserUseCase.getAllGithubUser()
                .collect { result ->
                    _githubUserState.value = result
                }
        }
    }
}