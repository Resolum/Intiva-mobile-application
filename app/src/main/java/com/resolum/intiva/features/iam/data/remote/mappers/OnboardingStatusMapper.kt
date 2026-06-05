package com.resolum.intiva.features.iam.data.remote.mappers

import com.resolum.intiva.features.iam.data.remote.models.OnboardingStatusResponseDto
import com.resolum.intiva.features.iam.domain.models.OnboardingStatus

/**
 * Mapper to convert OnboardingStatusResponseDto to OnboardingStatus domain model.
 */
fun OnboardingStatusResponseDto.toDomain(): OnboardingStatus {
    return OnboardingStatus(
        onboardingId = onboardingId,
        userId = userId,
        currentStep = currentStep,
        onboardingCompleted = onboardingCompleted,
        completedAt = completedAt
    )
}