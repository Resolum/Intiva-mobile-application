package com.resolum.intiva.features.savings.data.remote.services

import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.ContributionResponseDto
import com.resolum.intiva.features.savings.data.remote.models.SavingGoalResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit service interface for saving goal related API endpoints.
 *
 * Covers:
 * - GET  a specific saving goal by accountId and goalId
 * - POST a monetary contribution to a saving goal
 * - PATCH to mark a goal as completed or uncompleted 
 */
interface SavingGoalService {

    /**
     * Fetches all saving goals for an account.
     */
    @GET("accounts/{accountId}/saving-goals")
    suspend fun getSavingGoals(
        @Path("accountId") accountId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Fetches only completed saving goals for an account.
     */
    @GET("accounts/{accountId}/saving-goals/completed")
    suspend fun getCompletedSavingGoals(
        @Path("accountId") accountId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Fetches saving goals belonging to a family group.
     */
    @GET("accounts/{accountId}/saving-goals/group/{groupId}")
    suspend fun getGroupSavingGoals(
        @Path("accountId") accountId: Long,
        @Path("groupId") groupId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Creates a new saving goal.
     */
    @POST("accounts/{accountId}/saving-goals")
    suspend fun createSavingGoal(
        @Path("accountId") accountId: Long,
        @Body request: com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
    ): SavingGoalResponseDto

    /**
     * Fetches the details of a single saving goal.
     *
     * @param accountId  The ID of the account that owns the goal.
     * @param savingGoalId The ID of the saving goal to fetch.
     * @return A [SavingGoalResponseDto] with current progress, status and metadata.
     */
    @GET("accounts/{accountId}/saving-goals/{savingGoalId}")
    suspend fun getSavingGoal(
        @Path("accountId") accountId: Long,
        @Path("savingGoalId") savingGoalId: Long
    ): SavingGoalResponseDto

    /**
     * Registers a monetary contribution to a saving goal.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal to contribute to.
     * @param request      The contribution details (amount, currency, contributorId).
     */
    @POST("accounts/{accountId}/saving-goals/{savingGoalId}/contributions")
    suspend fun registerContribution(
        @Path("accountId") accountId: Long,
        @Path("savingGoalId") savingGoalId: Long,
        @Body request: ContributionRequestDto
    ): ContributionResponseDto

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal to complete.
     */
    @PATCH("accounts/{accountId}/saving-goals/{savingGoalId}/complete")
    suspend fun completeGoal(
        @Path("accountId") accountId: Long,
        @Path("savingGoalId") savingGoalId: Long
    )

    /**
     * Reverts a saving goal from COMPLETED back to UNCOMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal to uncomplete.
     */
    @PATCH("accounts/{accountId}/saving-goals/{savingGoalId}/uncomplete")
    suspend fun uncompleteGoal(
        @Path("accountId") accountId: Long,
        @Path("savingGoalId") savingGoalId: Long
    )
}
