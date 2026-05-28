package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

/**
 * Resolves the authenticated user ID for use in saving goal API calls.
 *
 * All saving goal endpoints are scoped under /api/v1/users/{userId}, so this use case
 * simply retrieves the authenticated userId from SessionRepository.
 */
class GetSavingsAccountIdUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    /**
     * Returns the authenticated user's ID.
     *
     * @return The user ID to use as the path parameter in saving goal endpoints.
     * @throws IllegalStateException if the user is not authenticated and no user ID is stored.
     */
    suspend operator fun invoke(): Long {
        return sessionRepository.getUserId()
            ?: throw IllegalStateException("User ID not found in session. Please sign in again.")
    }
}
