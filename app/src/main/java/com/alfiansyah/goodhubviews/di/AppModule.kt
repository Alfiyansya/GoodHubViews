package com.alfiansyah.goodhubviews.di

import com.alfiansyah.goodhubviews.core.domain.usecase.GithubUserInteractor
import com.alfiansyah.goodhubviews.core.domain.usecase.GithubUserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule{

    @Binds
    @ViewModelScoped
    abstract fun provideUserUseCase(githubUserInteractor: GithubUserInteractor): GithubUserUseCase
}