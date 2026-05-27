package com.resolum.intiva.features.savings.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.math.BigDecimal

/**
 * Domain repository interface for saving goal operations.
 *
 * Defines the contract that [SavingGoalRepositoryImpl] must fulfill,
 * keeping the domain layer decoupled from any data-source details.
 */
interface SavingGoalRepository {

    /** Fetches all saving goals for the given account. */
    suspend fun getSavingGoals(accountId: Long): NetworkResult<List<SavingGoal>>

    /** Fetches only completed saving goals. */
    suspend fun getCompletedSavingGoals(accountId: Long): NetworkResult<List<SavingGoal>>

    /** Fetches saving goals for a family group. */
    suspend fun getGroupSavingGoals(accountId: Long, groupId: Long): NetworkResult<List<SavingGoal>>

    /** Creates a new saving goal. */
    suspend fun createSavingGoal(
        accountId: Long,
        title: String,
        targetAmount: java.math.BigDecimal,
        currencyCode: String,
        deadline: String,
        ownerType: String,
        categoryId: Long,
        description: String = ""
    ): NetworkResult<SavingGoal>

    /**
     * Fetches the details of a saving goal.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the saving goal.
     * @return A [NetworkResult] wrapping the [SavingGoal] domain model.
     */
    suspend fun getSavingGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<SavingGoal>

    /**
     * Registers a monetary contribution to a saving goal.
     * Performs validation: amount must be greater than zero.
     *
     * @param accountId     The ID of the account.
     * @param savingGoalId  The ID of the goal to contribute to.
     * @param amount        The contribution amount (must be > 0).
     * @param currencyCode  ISO 4217 code (e.g. "USD", "PEN").
     * @param contributorId The ID of the contributing user.
     * @return A [NetworkResult] wrapping the created [GoalContribution] on success.
     */
    suspend fun registerContribution(
        accountId: Long,
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<GoalContribution>

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    suspend fun completeGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit>

    /**
     * Reverts a saving goal from COMPLETED to UNCOMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    suspend fun uncompleteGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit>
}
