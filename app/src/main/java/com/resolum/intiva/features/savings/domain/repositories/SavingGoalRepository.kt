package com.resolum.intiva.features.savings.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.math.BigDecimal

/**
 * Domain repository interface for saving goal operations.
 *
 * Defines the contract that [SavingGoalRepositoryImpl] must fulfill,
 * keeping the domain layer decoupled from any data-source details.
 */
interface SavingGoalRepository {

    /** GET /saving-goals?userId={userId} — personal goals for the given user. */
    suspend fun getSavingGoals(userId: Long): NetworkResult<List<SavingGoal>>

    /** GET /saving-goals/completed?userId={userId} */
    suspend fun getCompletedSavingGoals(userId: Long): NetworkResult<List<SavingGoal>>

    /** GET /saving-goals/group/{groupId} */
    suspend fun getGroupSavingGoals(groupId: String): NetworkResult<List<SavingGoal>>

    /** GET /saving-goals/{savingGoalId} */
    suspend fun getSavingGoal(savingGoalId: Long): NetworkResult<SavingGoal>

    /** POST /saving-goals */
    suspend fun createSavingGoal(
        ownerType: String,
        actorUserId: Long,
        ownerId: String,
        title: String,
        targetAmount: BigDecimal,
        currencyCode: String,
        deadline: String,
        categoryId: Long,
        description: String = ""
    ): NetworkResult<SavingGoal>

    /**
     * POST /saving-goals/{savingGoalId}/contributions
     *
     * The API returns 201 with an empty body; after posting we re-fetch the goal
     * so the UI can reflect the updated [currentAmount] and [status].
     */
    suspend fun registerContribution(
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<SavingGoal>

    /** PATCH /saving-goals/{savingGoalId}/complete */
    suspend fun completeGoal(savingGoalId: Long): NetworkResult<Unit>

    /** PATCH /saving-goals/{savingGoalId}/uncomplete */
    suspend fun uncompleteGoal(savingGoalId: Long): NetworkResult<Unit>

    /** DELETE /saving-goals/{savingGoalId} */
    suspend fun deleteSavingGoal(savingGoalId: Long): NetworkResult<Unit>

    /**
     * PATCH /saving-goals/{savingGoalId}
     * Updates title, description and/or targetAmount. Only non-null fields are sent.
     */
    suspend fun updateSavingGoal(
        savingGoalId: Long,
        title: String?,
        description: String?,
        newTargetAmount: BigDecimal?
    ): NetworkResult<Unit>
}
