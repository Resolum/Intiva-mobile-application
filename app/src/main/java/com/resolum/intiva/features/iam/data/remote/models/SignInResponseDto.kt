package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for sign-in responses.
 *
 * @property userId The unique identifier of the signed-in user.
 * @property email The email address of the signed-in user.
 * @property token The authentication token for the signed-in user.
 */
data class SignInResponseDto(
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String,
)
