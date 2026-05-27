package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Use case for fetching a single saving goal by account and goal ID.
 *
 * Follows the single [invoke] pattern of [com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetCategoriesUseCase].
 */
class GetSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    /**
     * @param userId       The ID of the user that owns the goal.
     * @param savingGoalId The ID of the saving goal.
     * @return [NetworkResult.Success] with [SavingGoal] or [NetworkResult.Error] on failure.
     */
    suspend operator fun invoke(
        userId: Long,
        savingGoalId: Long
    ): NetworkResult<SavingGoal> = repository.getSavingGoal(userId, savingGoalId)
}
