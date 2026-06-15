package com.resolum.intiva.features.profiles.data.remote.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("age") val age: Int? = null
)
