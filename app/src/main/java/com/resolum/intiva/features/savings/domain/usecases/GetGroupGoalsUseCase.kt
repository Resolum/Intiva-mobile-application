package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Fetches saving goals that belong to a family group.
 */
class GetGroupGoalsUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(userId: Long, groupId: Long): NetworkResult<List<SavingGoal>> =
        repository.getGroupSavingGoals(userId, groupId)
}
