package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Updates a saving goal's title, description and/or target amount
 *
 * Only non-null fields are sent to the API.
 */
class UpdateSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(
        savingGoalId: Long,
        title: String?,
        description: String?,
        newTargetAmount: BigDecimal?
    ): NetworkResult<Unit> = repository.updateSavingGoal(savingGoalId, title, description, newTargetAmount)
}
