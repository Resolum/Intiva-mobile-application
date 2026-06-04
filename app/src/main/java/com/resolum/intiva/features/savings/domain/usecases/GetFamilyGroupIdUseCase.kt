package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import com.resolum.intiva.features.shared.domain.model.OwnerType
import javax.inject.Inject

/**
 * Resolves the family [groupId] used by the group savings goals endpoint.
 *
 * Reads from [SessionRepository] first; if missing, picks the first category
 * that has a non-null [com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category.groupId].
 */
class GetFamilyGroupIdUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<Long> {
        sessionRepository.getGroupId()?.let { return NetworkResult.Success(it) }

        return when (
            val result = categoryRepository.getCategoriesByOwnerId(
                ownerType = OwnerType.FAMILY.name,
                type = TransactionType.EXPENSE.name
            )
        ) {
            is NetworkResult.Success -> {
                val groupId = result.data.firstNotNullOfOrNull { it.groupId }
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
