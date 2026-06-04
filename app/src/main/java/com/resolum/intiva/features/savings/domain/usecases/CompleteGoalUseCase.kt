package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/** Marks a saving goal as COMPLETED via PATCH /saving-goals/{savingGoalId}/complete. */
class CompleteGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(savingGoalId: Long): NetworkResult<Unit> =
        repository.completeGoal(savingGoalId)
}
