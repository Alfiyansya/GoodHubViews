package com.alfiansyah.goodhubviews.core.domain.usecase

import com.alfiansyah.goodhubviews.core.domain.repository.IGithubUserRepository
import javax.inject.Inject

class GithubUserInteractor @Inject constructor(private val githubUserRepository: IGithubUserRepository): GithubUserUseCase {
    override fun getAllGithubUser() = githubUserRepository.getAllGithubUser()
    override fun getDetailGithubUser(username: String) =githubUserRepository.getDetailGithubUser(username)
    override fun getSearchGithubUser(query: String)= githubUserRepository.getUserBySearch(query)
}