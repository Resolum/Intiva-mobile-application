package com.resolum.intiva.features.iam.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.OnboardingRepository
import javax.inject.Inject

/**
 * Use case for advancing the onboarding step of a user.
 *
 * @property onboardingRepository The repository responsible for managing onboarding-related operations.
 */
class AdvanceOnboardingStepUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    /**
     * Advances the onboarding step for the user.
     *
     * @return A [NetworkResult] indicating the success or failure of the operation.
     */
    suspend operator fun invoke() {
        onboardingRepository.advanceOnboardingStep()
    }
}