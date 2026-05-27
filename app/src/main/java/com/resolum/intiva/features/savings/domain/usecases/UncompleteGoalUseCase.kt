package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Use case for reverting a saving goal from COMPLETED to UNCOMPLETED (US-022).
 *
 * Symmetric counterpart to [CompleteGoalUseCase]. Delegates directly to the
 * repository without additional business logic.
 */
class UncompleteGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    /**
     * Reverts the specified saving goal to UNCOMPLETED via PATCH endpoint.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal to uncomplete.
     * @return [NetworkResult.Success] on success, [NetworkResult.Error] otherwise.
     */
    suspend operator fun invoke(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit> = repository.uncompleteGoal(accountId, savingGoalId)
}
