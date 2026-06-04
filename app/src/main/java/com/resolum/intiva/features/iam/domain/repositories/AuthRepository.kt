package com.resolum.intiva.features.iam.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.domain.models.SignInRequest
import com.resolum.intiva.features.iam.domain.models.SignInResult
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.models.SignUpResult

/**
 * Repository interface for authentication-related operations.
 */
interface AuthRepository {

    /**
     * Signs up a new user with the provided [request] data.
     *
     * @param request The sign-up request containing user details.
     * @return A [NetworkResult] containing either a successful [SignUpResponseDto] or an error message.
     */
    suspend fun signUp(request: SignUpRequest): NetworkResult<SignUpResult>

    /**
     * Signs in a user with the provided [request] data.
     *
     * @param request The sign-in request containing user credentials.
     * @return A [NetworkResult] containing either a successful [SignInResponseDto] or an error message.
     */
    suspend fun signIn(request: SignInRequest) : NetworkResult<SignInResult>
}