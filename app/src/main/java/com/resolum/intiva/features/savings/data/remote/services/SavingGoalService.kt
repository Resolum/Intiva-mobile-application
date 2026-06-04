package com.resolum.intiva.features.savings.data.remote.services

import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
import com.resolum.intiva.features.savings.data.remote.models.SavingGoalResponseDto
import com.resolum.intiva.features.savings.data.remote.models.UpdateSavingGoalRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for saving goal related API endpoints.
 *
 * All paths are relative to the base URL (which already includes /api/v1/).
 */
interface SavingGoalService {

    /** GET /api/v1/saving-goals?userId={userId} */
    @GET("saving-goals")
    suspend fun getSavingGoals(
        @Query("userId") userId: Long
    ): List<SavingGoalResponseDto>

    /** GET /api/v1/saving-goals/completed?userId={userId} */
    @GET("saving-goals/completed")
    suspend fun getCompletedSavingGoals(
        @Query("userId") userId: Long
    ): List<SavingGoalResponseDto>

    /** GET /api/v1/saving-goals/group/{groupId} */
    @GET("saving-goals/group/{groupId}")
    suspend fun getGroupSavingGoals(
        @Path("groupId") groupId: String
    ): List<SavingGoalResponseDto>

    /** GET /api/v1/saving-goals/{savingGoalId} */
    @GET("saving-goals/{savingGoalId}")
    suspend fun getSavingGoal(
        @Path("savingGoalId") savingGoalId: Long
    ): SavingGoalResponseDto

    /** POST /api/v1/saving-goals */
    @POST("saving-goals")
    suspend fun createSavingGoal(
        @Body request: CreateSavingGoalRequestDto
    ): SavingGoalResponseDto

    /**
     * POST /api/v1/saving-goals/{savingGoalId}/contributions
     *
     * Returns 201 with empty body on success; we use Unit since the response schema is {}.
     */
    @POST("saving-goals/{savingGoalId}/contributions")
    suspend fun registerContribution(
        @Path("savingGoalId") savingGoalId: Long,
        @Body request: ContributionRequestDto
    )

    /** PATCH /api/v1/saving-goals/{savingGoalId}/complete */
    @PATCH("saving-goals/{savingGoalId}/complete")
    suspend fun completeGoal(
        @Path("savingGoalId") savingGoalId: Long
    )

    /** PATCH /api/v1/saving-goals/{savingGoalId}/uncomplete */
    @PATCH("saving-goals/{savingGoalId}/uncomplete")
    suspend fun uncompleteGoal(
        @Path("savingGoalId") savingGoalId: Long
    )

    /** DELETE /api/v1/saving-goals/{savingGoalId} */
    @DELETE("saving-goals/{savingGoalId}")
    suspend fun deleteSavingGoal(
        @Path("savingGoalId") savingGoalId: Long
    )

    /** PATCH /api/v1/saving-goals/{savingGoalId} — updates title, description and/or targetAmount */
    @PATCH("saving-goals/{savingGoalId}")
    suspend fun updateSavingGoal(
        @Path("savingGoalId") savingGoalId: Long,
        @Body request: UpdateSavingGoalRequestDto
    )
}
