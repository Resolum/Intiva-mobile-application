package com.resolum.intiva.features.iam.data.remote

import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.data.remote.services.AuthService
import javax.inject.Inject

/**
 * Facade service for authentication-related operations.
 *
 * This service abstracts the underlying authentication mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles user registration, login, and other
 * authentication-related tasks by delegating to the appropriate services.
 */
class AuthFacadeService @Inject constructor(
    private val authService: AuthService
) {

    /**
     * Registers a new user with the provided sign-up information.
     *
     * @param dto The data transfer object containing the user's sign-up information.
     * @return A [SignUpResponseDto] containing the result of the sign-up operation.
     * @throws Exception if the response body is empty or if the HTTP request fails.
     */
    suspend fun signUp(
        dto: SignUpRequestDto
    ) : SignUpResponseDto {
        val response = authService.signUp(dto)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw retrofit2.HttpException(response)
        }
    }
}