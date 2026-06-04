package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the server after a successful user sign-up.
 * Contains the user's ID, email, and authentication token.
 * @param id The unique identifier of the user.
 * @param email The user's email address.
 */
data class SignUpResponseDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("email")
    val email: String
)
