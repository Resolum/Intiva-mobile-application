package com.resolum.intiva.features.iam.domain.models

/**
 * Represents the request to advance the onboarding process for a user.
 *
 * @property userId The unique identifier for the user whose onboarding process is to be advanced.
 */
data class AdvanceOnboardingRequest(
    val userId: Long,
)
