package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Fetches saving goals with status COMPLETED for an account.
 */
class GetCompletedGoalsUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(userId: Long): NetworkResult<List<SavingGoal>> =
        repository.getCompletedSavingGoals(userId)
}
