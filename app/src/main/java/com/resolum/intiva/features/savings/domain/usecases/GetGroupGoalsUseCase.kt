package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/** Fetches saving goals for a family group via GET /saving-goals/group/{groupId}. */
class GetGroupGoalsUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(groupId: String): NetworkResult<List<SavingGoal>> =
        repository.getGroupSavingGoals(groupId)
}
