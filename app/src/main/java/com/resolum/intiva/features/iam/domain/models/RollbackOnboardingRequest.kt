package com.resolum.intiva.features.iam.domain.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a request to roll back the onboarding process for a user.
 *
 * @property userId The unique identifier of the user for whom the onboarding process should be rolled back.
 */
data class RollbackOnboardingRequest(
    val userId: Long
)
