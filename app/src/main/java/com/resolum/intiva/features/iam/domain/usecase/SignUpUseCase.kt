package com.resolum.intiva.features.iam.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.network.model.map
import com.resolum.intiva.features.iam.data.remote.mappers.toDomain
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.models.SignUpResult
import com.resolum.intiva.features.iam.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Use case for signing up a new user.
 *
 * Validates the input and calls the repository to perform the sign-up operation.
 *
 * @param repository The authentication repository to handle sign-up requests.
 */
class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Invokes the sign-up process with the given request.
     *
     * @param request The sign-up request containing email and password.
     * @return A [NetworkResult] containing either a [SignUpResult] on success or an error message on failure.
     */
    suspend operator fun invoke(request: SignUpRequest): NetworkResult<SignUpResult> {
        if (request.email.isBlank() || request.password.isBlank()) {
            return NetworkResult.Error("Email and password must not be blank")
        }
        if (!request.email.contains("@")) {
            return NetworkResult.Error("Invalid email format")
        }
        if (request.password.length < 8) {
            return NetworkResult.Error("Password must be at least 8 characters")
        }

        return repository.signUp(request)
    }
}
