package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Registers a monetary contribution to a saving goal (US-021).
 *
 * Validates [amount] > 0, then delegates to the repository which posts to
 * POST /saving-goals/{savingGoalId}/contributions and re-fetches the updated goal.
 */
class RegisterContributionUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<SavingGoal> {
        if (amount <= BigDecimal.ZERO) {
            return NetworkResult.Error(
                message = "El monto debe ser mayor a cero.",
                code = 400
            )
        }
        return repository.registerContribution(
            savingGoalId = savingGoalId,
            amount = amount,
            currencyCode = currencyCode,
            contributorId = contributorId
        )
    }
}
