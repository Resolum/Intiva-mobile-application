package com.resolum.intiva.features.profiles.data.remote.services

import com.resolum.intiva.features.profiles.data.remote.models.AdvanceOnboardingRequestDto
import com.resolum.intiva.features.profiles.data.remote.models.OnboardingStatusResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

/**
 * Retrofit service interface for onboarding-related API calls.
 * This interface defines the endpoints for retrieving the onboarding status and advancing the onboarding process for a user.
 */
interface OnboardingService {

    /**
     * Makes a GET request to the "onboardings/status" endpoint to retrieve the onboarding status for a specific user.
     *
     * @param userId The unique identifier of the user for whom to retrieve the onboarding status.
     * @return An [OnboardingStatusResponseDto] containing the onboarding status information for the specified user.
     */
    @GET("onboardings/status")
    suspend fun getOnboardingStatus(
        @Query("userId") userId: Long
    ): OnboardingStatusResponseDto

    /**
     * Makes a PATCH request to the "onboardings/advances" endpoint to advance the onboarding process for a specific user.
     *
        * @param response An [AdvanceOnboardingRequestDto] containing the necessary information to advance the onboarding process for the user.
     */
    @PATCH("onboardings/advances")
    suspend fun advanceOnboarding(
        @Body response: AdvanceOnboardingRequestDto
    )

    /**
     * Makes a PATCH request to the "onboardings/skips" endpoint to skip the onboarding process for a specific user.
     *
     * @param response An [AdvanceOnboardingRequestDto] containing the user whose onboarding process should be skipped.
     */
    @PATCH("onboardings/skips")
    suspend fun skipOnboarding(
        @Body response: AdvanceOnboardingRequestDto
    )

    /**
     * Makes a PATCH request to the "onboardings/rollbacks" endpoint to roll back the onboarding process for a specific user.
     *
     * @param response An [AdvanceOnboardingRequestDto] containing the necessary information to roll back the onboarding process for the user.
     */
    @PATCH("onboardings/rollbacks")
    suspend fun rollbackOnboarding(
        @Body response: AdvanceOnboardingRequestDto
    )
}
