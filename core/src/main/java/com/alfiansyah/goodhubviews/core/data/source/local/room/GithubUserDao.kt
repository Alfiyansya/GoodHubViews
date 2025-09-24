package com.alfiansyah.goodhubviews.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.alfiansyah.goodhubviews.core.data.source.local.entity.GithubUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_user")
    fun getAllUser(): Flow<List<GithubUserEntity>>

    @Query("SELECT * FROM github_user WHERE login = :username")
    fun getDetailGithubUser(username: String): Flow<GithubUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGithubUserList(githubUser: List<GithubUserEntity>)

    @Upsert
    suspend fun upsertGithubUser(githubUser: GithubUserEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailGithubUser(githubUser: GithubUserEntity)
}