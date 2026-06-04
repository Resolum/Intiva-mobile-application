package com.resolum.intiva.features.iam.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the request body for rolling back the onboarding process for a user.
 *
 * @property userId The unique identifier for the user whose onboarding process is to be rolled back.
 */
data class RollbackOnboardingRequestDto(
    @SerializedName("userId")
    val userId: Long
)