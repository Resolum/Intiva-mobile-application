package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/** Reverts a saving goal to UNCOMPLETED via PATCH /saving-goals/{savingGoalId}/uncomplete. */
class UncompleteGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(savingGoalId: Long): NetworkResult<Unit> =
        repository.uncompleteGoal(savingGoalId)
}
