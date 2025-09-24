package com.alfiansyah.goodhubviews.core.utils

import com.alfiansyah.goodhubviews.core.data.source.local.entity.GithubUserEntity
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserDetailResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserItemResponse
import com.alfiansyah.goodhubviews.core.data.source.remote.response.GithubUserSearchResponse
import com.alfiansyah.goodhubviews.core.domain.model.GithubUser

object DataMapper {
    fun mapResponsesToEntities(input: List<GithubUserItemResponse>): List<GithubUserEntity> {
        val githubUserList = ArrayList<GithubUserEntity>()
        input.map {
            val githubUserEntity =
                GithubUserEntity(
                    id = it.id,
                    gistsUrl = it.gistsUrl,
                    reposUrl = it.reposUrl,
                    followingUrl = it.followingUrl,
                    starredUrl = it.starredUrl,
                    login = it.login,
                    followersUrl = it.followersUrl,
                    type = it.type,
                    url = it.url,
                    subscriptionsUrl = it.subscriptionsUrl,
                    receivedEventsUrl = it.receivedEventsUrl,
                    avatarUrl = it.avatarUrl,
                    eventsUrl = it.eventsUrl,
                    htmlUrl = it.htmlUrl,
                    siteAdmin = it.siteAdmin,
                    gravatarId = it.gravatarId,
                    nodeId = it.nodeId,
                    organizationsUrl = it.organizationsUrl,
                    isFavorite = false
                )
            githubUserList.add(githubUserEntity)
        }
        return githubUserList
    }
    fun mapResponsesToEntities(input: GithubUserDetailResponse): GithubUserEntity {
        val githubUserList = ArrayList<GithubUserEntity>()
            val githubUserEntity =
                GithubUserEntity(
                    gistsUrl= input.gistsUrl,
                    reposUrl= input.reposUrl,
                    userViewType= input.userViewType,
                    followingUrl= input.followingUrl,
                    twitterUsername= input.twitterUsername,
                    bio= input.bio.toString(),
                    createdAt= input.createdAt,
                    login= input.login,
                    type= input.type,
                    blog= input.blog,
                    subscriptionsUrl= input.subscriptionsUrl,
                    updatedAt= input.updatedAt,
                    siteAdmin= input.siteAdmin,
                    company= input.company,
                    id= input.id,
                    publicRepos= input.publicRepos,
                    gravatarId= input.gravatarId,
                    email= input.email.toString(),
                    organizationsUrl= input.organizationsUrl,
                    hireable= input.hireable.toString(),
                    starredUrl= input.starredUrl,
                    followersUrl= input.followersUrl,
                    publicGists= input.publicGists,
                    url= input.url,
                    receivedEventsUrl= input.receivedEventsUrl,
                    followers= input.followers,
                    avatarUrl= input.avatarUrl,
                    eventsUrl= input.eventsUrl,
                    htmlUrl= input.htmlUrl,
                    following= input.following,
                    name= input.name,
                    location= input.location,
                    nodeId= input.nodeId,
                    isFavorite = false
                )
            githubUserList.add(githubUserEntity)
        return githubUserEntity
    }

    fun mapEntitiesToDomain(input: List<GithubUserEntity?>): List<GithubUser> =
        input.map {
            GithubUser(
                id = it?.id,
                gistsUrl = it?.gistsUrl,
                reposUrl = it?.reposUrl,
                followingUrl = it?.followingUrl,
                starredUrl = it?.starredUrl,
                login = it?.login,
                followersUrl = it?.followersUrl,
                type = it?.type,
                url = it?.url,
                subscriptionsUrl = it?.subscriptionsUrl,
                receivedEventsUrl = it?.receivedEventsUrl,
                avatarUrl = it?.avatarUrl,
                eventsUrl = it?.eventsUrl,
                htmlUrl = it?.htmlUrl,
                siteAdmin = it?.siteAdmin,
                gravatarId = it?.gravatarId,
                nodeId = it?.nodeId,
                organizationsUrl = it?.organizationsUrl,
                isFavorite = it?.isFavorite
            )
        }

    fun mapEntitiesToDomain(input: GithubUserEntity): GithubUser =
            GithubUser(
                gistsUrl = input.gistsUrl,
                reposUrl = input.reposUrl,
                userViewType = input.userViewType,
                followingUrl = input.followingUrl,
                twitterUsername = input.twitterUsername,
                bio = input.bio,
                createdAt = input.createdAt,
                login = input.login,
                type = input.type,
                blog = input.blog,
                subscriptionsUrl = input.subscriptionsUrl,
                updatedAt = input.updatedAt,
                siteAdmin = input.siteAdmin,
                company = input.company,
                id = input.id,
                publicRepos = input.publicRepos,
                gravatarId = input.gravatarId,
                email = input.email,
                organizationsUrl = input.organizationsUrl,
                hireable = input.hireable,
                starredUrl = input.starredUrl,
                followersUrl = input.followersUrl,
                publicGists = input.publicGists,
                url = input.url,
                receivedEventsUrl = input.receivedEventsUrl,
                followers = input.followers,
                avatarUrl = input.avatarUrl,
                eventsUrl = input.eventsUrl,
                htmlUrl = input.htmlUrl,
                following = input.following,
                name = input.name,
                location = input.location,
                nodeId = input.nodeId,
                isFavorite = input.isFavorite
            )
    fun mapSearchResponseToDomain(input: GithubUserSearchResponse): List<GithubUser> {
        val searchUserList = ArrayList<GithubUser>()
        input.userItems?.map {
            val searchUser =
                GithubUser(
                    id = it.id,
                    login = it.login,
                    avatarUrl = it.avatarUrl,
                )
            searchUserList.add(searchUser)
        }
        return searchUserList
    }
}