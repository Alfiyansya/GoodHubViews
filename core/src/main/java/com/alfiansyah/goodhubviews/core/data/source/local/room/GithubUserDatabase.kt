package com.alfiansyah.goodhubviews.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alfiansyah.goodhubviews.core.data.source.local.entity.GithubUserEntity

@Database(entities = [GithubUserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDatabase: RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
}