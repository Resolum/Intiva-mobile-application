package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/** Result of a successful contribution: the created record and the refreshed goal. */
data class ContributionResult(
    val contribution: GoalContribution,
    val updatedGoal: SavingGoal
)

/**
 * Use case for registering a monetary contribution to a saving goal (US-021).
 *
 * Validates that the contribution [amount] is strictly greater than zero before
 * delegating to the repository. Returns a [NetworkResult.Error] immediately if
 * validation fails, preventing an unnecessary network call.
 *
 * After a successful contribution, this use case also re-fetches the goal so the
 * caller can refresh [currentAmount] and the contribution history list in the UI.
 */
class RegisterContributionUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    /**
     * Validates the [amount] and, if valid, posts the contribution.
     * Then re-fetches the updated [SavingGoal] so the detail screen reflects
     * the new [currentAmount].
     *
     * @param accountId     The ID of the account.
     * @param savingGoalId  The ID of the goal to contribute to.
     * @param amount        The monetary amount — must be > 0.
     * @param currencyCode  ISO 4217 currency code.
     * @param contributorId ID of the contributing user.
     * @return [NetworkResult.Success] with the refreshed [SavingGoal], or
     *         [NetworkResult.Error] on validation failure or API error.
     */
    suspend operator fun invoke(
        accountId: Long,
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<ContributionResult> {
        // Validation: amount must be greater than zero
        if (amount <= BigDecimal.ZERO) {
            return NetworkResult.Error(
                message = "Contribution amount must be greater than zero.",
                code = 400
            )
        }

        // Post the contribution; return immediately on error
        val contributionResult = repository.registerContribution(
            accountId = accountId,
            savingGoalId = savingGoalId,
            amount = amount,
            currencyCode = currencyCode,
            contributorId = contributorId
        )
        val contribution = when (contributionResult) {
            is NetworkResult.Success -> contributionResult.data
            is NetworkResult.Error -> return contributionResult
        }

        // Re-fetch the goal so the UI can update currentAmount and progress
        return when (val goalResult = repository.getSavingGoal(accountId, savingGoalId)) {
            is NetworkResult.Success -> NetworkResult.Success(
                ContributionResult(
                    contribution = contribution,
                    updatedGoal = goalResult.data
                )
            )
            is NetworkResult.Error -> goalResult
        }
    }
}
