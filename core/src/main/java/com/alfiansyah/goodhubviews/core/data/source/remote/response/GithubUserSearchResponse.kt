package com.alfiansyah.goodhubviews.core.data.source.remote.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
data class GithubUserSearchResponse(
    @Json(name = "items")
    val userItems: List<UserSearchItemResponse>?,
    @Json(name = "total_count")
    val totalCount: Int?
)

@Parcelize
class UserSearchItemResponse(
    @Json(name = "avatar_url")
    val avatarUrl: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "login")
    val login: String?,
) : Parcelable
