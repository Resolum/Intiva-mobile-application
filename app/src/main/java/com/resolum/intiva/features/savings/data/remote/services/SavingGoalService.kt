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
 * - GET  a specific saving goal by userId and goalId
 * - POST a monetary contribution to a saving goal
 * - PATCH to mark a goal as completed or uncompleted
 */
interface SavingGoalService {

    /**
     * Fetches all saving goals for a user.
     */
    @GET("users/{userId}/saving-goals")
    suspend fun getSavingGoals(
        @Path("userId") userId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Fetches only completed saving goals for a user.
     */
    @GET("users/{userId}/saving-goals/completed")
    suspend fun getCompletedSavingGoals(
        @Path("userId") userId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Fetches saving goals belonging to a family group.
     */
    @GET("users/{userId}/saving-goals/group/{groupId}")
    suspend fun getGroupSavingGoals(
        @Path("userId") userId: Long,
        @Path("groupId") groupId: Long
    ): List<SavingGoalResponseDto>

    /**
     * Creates a new saving goal.
     */
    @POST("users/{userId}/saving-goals")
    suspend fun createSavingGoal(
        @Path("userId") userId: Long,
        @Body request: com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
    ): SavingGoalResponseDto

    /**
     * Fetches the details of a single saving goal.
     *
     * @param userId       The ID of the user that owns the goal.
     * @param savingGoalId The ID of the saving goal to fetch.
     * @return A [SavingGoalResponseDto] with current progress, status and metadata.
     */
    @GET("users/{userId}/saving-goals/{savingGoalId}")
    suspend fun getSavingGoal(
        @Path("userId") userId: Long,
        @Path("savingGoalId") savingGoalId: Long
    ): SavingGoalResponseDto

    /**
     * Registers a monetary contribution to a saving goal.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal to contribute to.
     * @param request      The contribution details (amount, currency, contributorId).
     */
    @POST("users/{userId}/saving-goals/{savingGoalId}/contributions")
    suspend fun registerContribution(
        @Path("userId") userId: Long,
        @Path("savingGoalId") savingGoalId: Long,
        @Body request: ContributionRequestDto
    ): ContributionResponseDto

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal to complete.
     */
    @PATCH("users/{userId}/saving-goals/{savingGoalId}/complete")
    suspend fun completeGoal(
        @Path("userId") userId: Long,
        @Path("savingGoalId") savingGoalId: Long
    )

    /**
     * Reverts a saving goal from COMPLETED back to UNCOMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal to uncomplete.
     */
    @PATCH("users/{userId}/saving-goals/{savingGoalId}/uncomplete")
    suspend fun uncompleteGoal(
        @Path("userId") userId: Long,
        @Path("savingGoalId") savingGoalId: Long
    )
}
