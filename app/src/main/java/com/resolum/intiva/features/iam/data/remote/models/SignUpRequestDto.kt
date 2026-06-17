package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request body for user sign-up.
 */
data class SignUpRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("bio")
    val bio: String
)