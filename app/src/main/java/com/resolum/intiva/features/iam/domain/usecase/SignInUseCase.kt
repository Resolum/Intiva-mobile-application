package com.resolum.intiva.features.iam.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.models.SignInRequest
import com.resolum.intiva.features.iam.domain.models.SignInResult
import com.resolum.intiva.features.iam.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Use case for signing in an existing user.
 *
 * Validates the input and calls the repository to perform the sign-in operation.
 *
 * @param repository The authentication repository to handle sign-in requests.
 */
class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    /**
     * Invokes the sign-in process with the given request.
     *
     * @param request The sign-in request containing email and password.
     * @return A [NetworkResult] containing either a [SignInResult] on success or an error message on failure.
     */
    suspend operator fun invoke(request: SignInRequest): NetworkResult<SignInResult> {
        if (request.email.isBlank() || request.password.isBlank()) {
            return NetworkResult.Error("Email and password must not be blank")
        }
        if (!request.email.contains("@")) {
            return NetworkResult.Error("Invalid email format")
        }
        if (request.password.length < 8) {
            return NetworkResult.Error("Password must be at least 8 characters")
        }

        return repository.signIn(request)
    }
}