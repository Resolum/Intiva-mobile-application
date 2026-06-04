package com.resolum.intiva.features.iam.domain.models

/**
 * Data class representing a sign-in request with email and password.
 *
 * @property email The user's email address.
 * @property password The user's password.
 */
data class SignInRequest(
    val email: String,
    val password: String
)
