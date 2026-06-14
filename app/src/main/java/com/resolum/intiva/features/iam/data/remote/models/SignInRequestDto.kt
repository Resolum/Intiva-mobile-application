package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for sign-in requests.
 *
 * @property email The user's email address.
 * @property password The user's password.
 */
data class SignInRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
