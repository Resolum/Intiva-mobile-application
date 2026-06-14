package com.resolum.intiva.features.iam.domain.usecase

import com.resolum.intiva.features.iam.domain.repositories.OnboardingRepository
import javax.inject.Inject

/**
 * Use case for rolling back the onboarding step of a user.
 *
 * @property onboardingRepository The repository responsible for managing onboarding-related operations.
 */
class RollbackOnboardingStepUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    /**
     * Rolls back the onboarding step for the user.
     */
    suspend operator fun invoke() {
        onboardingRepository.rollbackOnboardingStep()
    }
}