package com.alfiansyah.goodhubviews.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alfiansyah.goodhubviews.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(githubUserUseCase: GithubUserUseCase) : ViewModel(){
    val githubUser = githubUserUseCase.getAllGithubUser().asLiveData()
}