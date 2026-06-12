package com.resolum.intiva.features.profiles.data.remote.models

import com.google.gson.annotations.SerializedName

data class ProfileResponseDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("email") val email: String?
)
