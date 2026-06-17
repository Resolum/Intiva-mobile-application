package com.resolum.intiva.features.iam.domain.models

/**
 * Data class representing a sign-up request with email and password.
 *
 * @property email The email address of the user.
 * @property password The password for the user's account.
 */
data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val age: Int,
    val phoneNumber: String,
    val bio: String
)