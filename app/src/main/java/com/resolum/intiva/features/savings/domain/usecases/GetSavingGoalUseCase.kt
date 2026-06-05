package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/** Fetches a single saving goal by its ID via GET /saving-goals/{savingGoalId}. */
class GetSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(savingGoalId: Long): NetworkResult<SavingGoal> =
        repository.getSavingGoal(savingGoalId)
}
