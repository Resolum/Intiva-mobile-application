package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/** Permanently deletes a saving goal via DELETE /saving-goals/{savingGoalId}. */
class DeleteSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(savingGoalId: Long): NetworkResult<Unit> =
        repository.deleteSavingGoal(savingGoalId)
}
