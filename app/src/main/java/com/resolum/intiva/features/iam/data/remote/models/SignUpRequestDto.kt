package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request body for user sign-up.
 * Contains the user's email and password.
 * @param email The user's email address.
 * @param password The user's password.
 */
data class SignUpRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
