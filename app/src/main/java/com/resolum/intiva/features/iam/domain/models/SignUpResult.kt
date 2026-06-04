package com.resolum.intiva.features.iam.domain.models

/**
 * Data class representing the result of a sign-up operation.
 *
 * @property id The unique identifier of the newly created user.
 * @property email The email address of the newly created user.
 */
data class SignUpResult(
    val id: Long,
    val email: String
)
