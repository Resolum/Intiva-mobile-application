package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Resolves the family [groupId] used by the group savings goals endpoint.
 *
 * Strategy:
 * 1. Returns the cached groupId from [SessionRepository] if available.
 * 2. Otherwise, fetches individual categories and looks for the first one whose
 *    ownerType is "family" — its [ownerId] is the groupId.
 * 3. Caches and returns the resolved groupId, or returns an error if none found.
 */
class GetFamilyGroupIdUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<Long> {
        sessionRepository.getGroupId()?.let { return NetworkResult.Success(it) }

        return when (val result = categoryRepository.getCategoriesByUserId()) {
            is NetworkResult.Success -> {
                val groupId = result.data
                    .firstOrNull { it.ownerType.equals("family", ignoreCase = true) }
                    ?.ownerId
                if (groupId == null) {
                    NetworkResult.Error(message = "No family group found for the current user.")
                } else {
                    sessionRepository.saveGroupId(groupId)
                    NetworkResult.Success(groupId)
                }
            }
            is NetworkResult.Error -> result
        }
    }
}
