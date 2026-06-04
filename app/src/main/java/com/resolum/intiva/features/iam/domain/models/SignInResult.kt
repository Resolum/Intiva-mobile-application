package com.resolum.intiva.features.iam.domain.models

/**
 * Represents the result of a successful sign-in operation.
 *
 * @property userId The unique identifier of the signed-in user.
 * @property email The email address of the signed-in user.
 * @property accessToken The access token issued for the signed-in user, used for authenticated requests.
 */
data class SignInResult(
    val userId: Long,
    val email: String,
    val accessToken: String,
)
