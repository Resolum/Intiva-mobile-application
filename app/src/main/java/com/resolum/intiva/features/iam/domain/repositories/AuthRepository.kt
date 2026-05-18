package com.resolum.intiva.features.iam.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
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
    suspend fun signUp(request: SignUpRequest): NetworkResult<SignUpResponseDto>
}