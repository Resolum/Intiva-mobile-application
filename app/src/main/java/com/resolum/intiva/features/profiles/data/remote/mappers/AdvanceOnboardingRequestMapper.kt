package com.resolum.intiva.features.profiles.data.remote.mappers

import com.resolum.intiva.features.profiles.data.remote.models.AdvanceOnboardingRequestDto
import com.resolum.intiva.features.profiles.domain.models.AdvanceOnboardingRequest

/**
 * Mapper function to convert an [AdvanceOnboardingRequest] domain model to an [AdvanceOnboardingRequestDto] data transfer object.
 *
 * @receiver AdvanceOnboardingRequest The domain model representing the advance onboarding request.
 * @return AdvanceOnboardingRequestDto The data transfer object representing the advance onboarding request.
 */
fun AdvanceOnboardingRequest.toDto(): AdvanceOnboardingRequestDto {
    return AdvanceOnboardingRequestDto(
        userId = userId
    )
}