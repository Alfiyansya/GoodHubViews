package com.alfiansyah.goodhubviews.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_user")
data class GithubUserEntity(

    @PrimaryKey
    @ColumnInfo(name="id")
    val id: Int? = null,

    @ColumnInfo(name="gistsUrl")
    val gistsUrl: String? = null,

    @ColumnInfo(name="repos_url")
    val reposUrl: String? = null,

    @ColumnInfo(name="userViewType")
    val userViewType: String? = null,

    @ColumnInfo(name="followingUrl")
    val followingUrl: String? = null,

    @ColumnInfo(name="twitterUsername")
    val twitterUsername: String? = null,

    @ColumnInfo(name="bio")
    val bio: String? = null,

    @ColumnInfo(name="createdAt")
    val createdAt: String? = null,

    @ColumnInfo(name="login")
    val login: String? = null,

    @ColumnInfo(name="type")
    val type: String? = null,

    @ColumnInfo(name="blog")
    val blog: String? = null,

    @ColumnInfo(name="subscriptionsUrl")
    val subscriptionsUrl: String? = null,

    @ColumnInfo(name="updatedAt")
    val updatedAt: String? = null,

    @ColumnInfo(name="siteAdmin")
    val siteAdmin: Boolean? = null,

    @ColumnInfo(name="company")
    val company: String? = null,

    @ColumnInfo(name="publicRepos")
    val publicRepos: Int? = null,

    @ColumnInfo(name="gravatarId")
    val gravatarId: String? = null,

    @ColumnInfo(name="email")
    val email: String? = null,

    @ColumnInfo(name="organizationsUrl")
    val organizationsUrl: String? = null,

    @ColumnInfo(name="hireable")
    val hireable: String? = null,

    @ColumnInfo(name="starredUrl")
    val starredUrl: String? = null,

    @ColumnInfo(name="followersUrl")
    val followersUrl: String? = null,

    @ColumnInfo(name="publicGists")
    val publicGists: Int? = null,

    @ColumnInfo(name="url")
    val url: String? = null,

    @ColumnInfo(name="received_events_url")
    val receivedEventsUrl: String? = null,

    @ColumnInfo(name="followers")
    val followers: Int? = null,

    @ColumnInfo(name="avatarUrl")
    val avatarUrl: String? = null,

    @ColumnInfo(name="eventsUrl")
    val eventsUrl: String? = null,

    @ColumnInfo(name="htmlUrl")
    val htmlUrl: String? = null,

    @ColumnInfo(name="following")
    val following: Int? = null,

    @ColumnInfo(name="name")
    val name: String? = null,

    @ColumnInfo(name="location")
    val location: String? = null,

    @ColumnInfo(name="nodeId")
    val nodeId: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)