package com.alfiansyah.goodhubviews.core.di

import com.alfiansyah.goodhubviews.core.data.repository.GithubUserRepository
import com.alfiansyah.goodhubviews.core.data.source.local.LocalDataSource
import com.alfiansyah.goodhubviews.core.data.source.remote.RemoteDataSource
import com.alfiansyah.goodhubviews.core.domain.repository.IGithubUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): IGithubUserRepository = GithubUserRepository(remoteDataSource,localDataSource)
}