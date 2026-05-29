package com.resolum.intiva.features.iam.domain.models

/**
 * Represents the onboarding status of a user in the IAM (Identity and Access Management) feature.
 *
 * @property onboardingId The unique identifier for the onboarding process.
 * @property userId The unique identifier for the user associated with the onboarding process.
 * @property currentStep The current step in the onboarding process that the user is on.
 * @property onboardingCompleted A boolean indicating whether the onboarding process has been completed.
 * @property completedAt The timestamp indicating when the onboarding process was completed, if applicable.
 */
data class OnboardingStatus(
    val onboardingId: String,
    val userId: String,
    val currentStep: String,
    val onboardingCompleted: Boolean,
    val completedAt: String?
)
