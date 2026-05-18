package com.resolum.intiva.features.iam.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.data.remote.AuthFacadeService
import com.resolum.intiva.features.iam.data.remote.mappers.toDomain
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.repositories.AuthRepository
import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.data.remote.services.AuthService
import com.resolum.intiva.features.iam.domain.models.SignUpResult
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface that interacts with the [AuthFacadeService]
 * to perform authentication-related operations.
 *
 * @property authFacadeService The service responsible for handling authentication API calls.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authFacadeService: AuthFacadeService
) : BaseRepository(), AuthRepository {

    /**
     * Signs up a new user using the provided [SignUpRequest] and returns a [NetworkResult]
     * containing the [SignUpResponseDto] on success or an error message on failure.
     *
     * @param request The sign-up request containing the user's email and password.
     * @return A [NetworkResult] with the sign-up response or an error message.
     */
    override suspend fun signUp(request: SignUpRequest): NetworkResult<SignUpResponseDto> =
        safeCall {
            authFacadeService.signUp(
                SignUpRequestDto(
                    email = request.email,
                    password = request.password,
                )
            )
        }
}