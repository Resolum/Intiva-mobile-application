package com.resolum.intiva.features.profiles.domain.usecase

import com.resolum.intiva.features.profiles.domain.repositories.OnboardingRepository
import jakarta.inject.Inject

/**
 * Use case to mark the onboarding process as completed.
 *
 * This use case interacts with the [OnboardingRepository] to update the onboarding status
 * in the data store, indicating that the user has completed the onboarding process.
 */
class CompleteOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) {
    /** Marks the onboarding process as completed by updating the data store.
     *
     * This function should be called when the user finishes the onboarding flow, ensuring that
     * they won't see it again on subsequent app launches.
     */
    suspend operator fun invoke() = onboardingRepository.setOnboardingShown()
}