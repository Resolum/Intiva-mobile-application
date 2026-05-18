package com.resolum.intiva.features.iam.data.repositories

import android.util.Log
import com.resolum.intiva.core.data.local.TokenDataStore
import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.network.model.map
import com.resolum.intiva.features.iam.data.remote.AuthFacadeService
import com.resolum.intiva.features.iam.data.remote.mappers.toDomain
import com.resolum.intiva.features.iam.data.remote.models.SignInRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.repositories.AuthRepository
import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.domain.models.SignInRequest
import com.resolum.intiva.features.iam.domain.models.SignInResult
import com.resolum.intiva.features.iam.domain.models.SignUpResult
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface that interacts with the [AuthFacadeService]
 * to perform authentication-related operations.
 *
 * @property authFacadeService The service responsible for handling authentication API calls.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authFacadeService: AuthFacadeService,
    private val tokenDataStore: TokenDataStore
) : BaseRepository(), AuthRepository {

    /**
     * Signs up a new user using the provided [SignUpRequest] and returns a [NetworkResult]
     * containing the [SignUpResponseDto] on success or an error message on failure.
     *
     * @param request The sign-up request containing the user's email and password.
     * @return A [NetworkResult] with the sign-up response or an error message.
     */
    override suspend fun signUp(request: SignUpRequest): NetworkResult<SignUpResult> =
        safeCall {
            authFacadeService.signUp(
                SignUpRequestDto(
                    email = request.email,
                    password = request.password
                )
            )
        }.map {
            it.toDomain()
        }

    /**
     * Signs in a user using the provided [SignInRequest] and returns a [NetworkResult]
     * containing the [SignInResult] on success or an error message on failure.
     *
     * On successful sign-in, the authentication token is saved to the [TokenDataStore] for future use.
     * @param request The sign-in request containing the user's email and password.
     * @return A [NetworkResult] with the sign-in response or an error message.
     */
    override suspend fun signIn(request: SignInRequest): NetworkResult<SignInResult> {
        val result = safeCall {
            authFacadeService.signIn(
                SignInRequestDto(
                    email = request.email,
                    password = request.password,
                )
            )
        }.map {
            it.toDomain()
        }

        if (result is NetworkResult.Success) {
            result.data.accessToken.let { token ->
                tokenDataStore.saveToken(token)
            }
        }

        return result
    }

}