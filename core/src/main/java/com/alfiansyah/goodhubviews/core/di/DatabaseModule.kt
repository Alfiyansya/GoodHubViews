package com.alfiansyah.goodhubviews.core.di

import android.content.Context
import androidx.room.Room
import com.alfiansyah.goodhubviews.core.data.source.local.room.GithubUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GithubUserDatabase =
        Room.databaseBuilder(context, GithubUserDatabase::class.java, "GithubUser.db").fallbackToDestructiveMigration().build()

    @Provides
    fun provideGithubUserDao(database: GithubUserDatabase) = database.githubUserDao()
}