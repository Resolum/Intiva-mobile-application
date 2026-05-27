package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Use case for marking a saving goal as COMPLETED (US-022).
 *
 * Follows the single-responsibility pattern of [GetCategoriesUseCase]: one
 * [invoke] operator, one repository call, no business logic beyond delegation.
 */
class CompleteGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    /**
     * Marks the specified saving goal as COMPLETED via PATCH endpoint.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal to mark complete.
     * @return [NetworkResult.Success] on success, [NetworkResult.Error] otherwise.
     */
    suspend operator fun invoke(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit> = repository.completeGoal(accountId, savingGoalId)
}
