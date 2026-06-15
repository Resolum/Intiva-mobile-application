package com.resolum.intiva.features.profiles.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.domain.models.OnboardingStatus
import com.resolum.intiva.features.profiles.domain.repositories.OnboardingRepository
import javax.inject.Inject

/**
 * Use case for retrieving the onboarding status of a user.
 *
 * @property onboardingRepository The repository responsible for fetching the onboarding status data.
 */
data class GetOnboardingStatusUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    /**
     * Retrieves the onboarding status of the user.
     *
     * @return A [NetworkResult] containing either a successful [OnboardingStatus] or an error message.
     */
    suspend operator fun invoke() : NetworkResult<OnboardingStatus> {
        return onboardingRepository.getOnboardingStatus()
    }
}
