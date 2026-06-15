package com.resolum.intiva.features.profiles.data.remote

import com.resolum.intiva.features.profiles.data.remote.models.AdvanceOnboardingRequestDto
import com.resolum.intiva.features.profiles.data.remote.services.OnboardingService
import javax.inject.Inject

/**
 * Facade service for onboarding-related operations.
 *
 * This service abstracts the underlying onboarding mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles fetching the onboarding status,
 * advancing the onboarding steps, and other onboarding-related tasks by delegating to the appropriate services.
 */
class OnboardingFacadeService @Inject constructor(
    private val onboardingService: OnboardingService
) {

    /** Fetches the onboarding status for the specified user ID. */
    suspend fun getOnboardingStatus(userId: Long) = onboardingService.getOnboardingStatus(userId)

    /** Advances the onboarding step for the specified user ID. */
    suspend fun advanceOnboardingStep(request: AdvanceOnboardingRequestDto) = onboardingService.advanceOnboarding(request)

    /** Skips the onboarding process for the specified user ID. */
    suspend fun skipOnboarding(request: AdvanceOnboardingRequestDto) = onboardingService.skipOnboarding(request)

    /** Rolls back the onboarding step for the specified user ID. */
    suspend fun rollbackOnboardingStep(request: AdvanceOnboardingRequestDto) = onboardingService.rollbackOnboarding(request)
}
