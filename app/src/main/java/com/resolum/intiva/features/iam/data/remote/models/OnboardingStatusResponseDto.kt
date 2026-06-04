package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the response from the onboarding status API endpoint.
 *
 * @property onboardingId The unique identifier for the onboarding process.
 * @property userId The unique identifier for the user associated with the onboarding process.
 * @property currentStep The current step in the onboarding process that the user is on.
 * @property onboardingCompleted A boolean indicating whether the onboarding process has been completed.
 * @property completedAt The timestamp indicating when the onboarding process was completed, if applicable.
 */
data class OnboardingStatusResponseDto(
    @SerializedName("onboardingId")
    val onboardingId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("currentStep")
    val currentStep: String,
    @SerializedName("onboardingCompleted")
    val onboardingCompleted: Boolean,
    @SerializedName("completedAt")
    val completedAt: String?
)
