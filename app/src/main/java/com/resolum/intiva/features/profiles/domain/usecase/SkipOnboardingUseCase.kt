package com.resolum.intiva.features.profiles.domain.usecase

import com.resolum.intiva.features.profiles.domain.repositories.OnboardingRepository
import javax.inject.Inject

/**
 * Use case for skipping the onboarding process of a user.
 */
class SkipOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    suspend operator fun invoke() {
        onboardingRepository.skipOnboarding()
    }
}
