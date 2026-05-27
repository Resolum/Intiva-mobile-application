package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

/**
 * Resolves the savings account ID.
 *
 * Backend optimization: userId and accountId share the exact same value.
 * Therefore, this use case simply retrieves the authenticated userId from SessionRepository
 * and returns it directly as the accountId, bypassing unnecessary financial account API lookups.
 */
class GetSavingsAccountIdUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    /**
     * Resolves the savings account ID directly from the authenticated session's user ID.
     *
     * @return The resolved account ID (which is the same as the user ID).
     * @throws IllegalStateException if the user is not authenticated and no user ID is stored.
     */
    suspend operator fun invoke(): Long {
        return sessionRepository.getUserId() 
            ?: throw IllegalStateException("User ID not found in session. Please sign in again.")
    }
}
