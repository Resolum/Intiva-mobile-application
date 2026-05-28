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

    /** Fetches all saving goals for the given user. */
    suspend fun getSavingGoals(userId: Long): NetworkResult<List<SavingGoal>>

    /** Fetches only completed saving goals. */
    suspend fun getCompletedSavingGoals(userId: Long): NetworkResult<List<SavingGoal>>

    /** Fetches saving goals for a family group. */
    suspend fun getGroupSavingGoals(userId: Long, groupId: Long): NetworkResult<List<SavingGoal>>

    /** Creates a new saving goal. */
    suspend fun createSavingGoal(
        userId: Long,
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
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the saving goal.
     * @return A [NetworkResult] wrapping the [SavingGoal] domain model.
     */
    suspend fun getSavingGoal(
        userId: Long,
        savingGoalId: Long
    ): NetworkResult<SavingGoal>

    /**
     * Registers a monetary contribution to a saving goal.
     * Performs validation: amount must be greater than zero.
     *
     * @param userId        The ID of the user.
     * @param savingGoalId  The ID of the goal to contribute to.
     * @param amount        The contribution amount (must be > 0).
     * @param currencyCode  ISO 4217 code (e.g. "USD", "PEN").
     * @param contributorId The ID of the contributing user.
     * @return A [NetworkResult] wrapping the created [GoalContribution] on success.
     */
    suspend fun registerContribution(
        userId: Long,
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<GoalContribution>

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    suspend fun completeGoal(
        userId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit>

    /**
     * Reverts a saving goal from COMPLETED to UNCOMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    suspend fun uncompleteGoal(
        userId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit>
}
