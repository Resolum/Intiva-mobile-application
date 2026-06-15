package com.resolum.intiva.features.profiles.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the request body for advancing the onboarding process for a user.
 *
 * @property userId The unique identifier for the user whose onboarding process is to be advanced.
 */
data class AdvanceOnboardingRequestDto(
    @SerializedName("userId")
    val userId: Long,
)
