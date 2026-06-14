package com.resolum.intiva.features.iam.data.remote

import com.resolum.intiva.features.iam.data.remote.models.SignInRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
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
     * Signs up a new user with the provided [SignUpRequestDto] and returns a [SignUpResponseDto]
     * containing the result of the sign-up operation.
     *
     * @param request The sign-up request containing the user's email and password.
     * @return A [SignUpResponseDto] with the result of the sign-up operation.
     */
    suspend fun signUp(request: SignUpRequestDto): SignUpResponseDto = authService.signUp(request)

    /**
     * Signs in an existing user with the provided [SignInRequestDto] and returns a [SignInResponseDto]
     * containing the result of the sign-in operation, including authentication tokens if successful.
     *
     * @param request The sign-in request containing the user's email and password.
     * @return A [SignInResponseDto] with the result of the sign-in operation.
     */
    suspend fun signIn(request: SignInRequestDto): SignInResponseDto = authService.signIn(request)
}