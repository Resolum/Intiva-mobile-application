package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing a contribution response from the API.
 *
 * Returned by:
 * POST /api/v1/users/{userId}/saving-goals/{savingGoalId}/contributions
 */
data class ContributionResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("contributorId") val contributorId: Long,
    @SerializedName("contributedAt") val contributedAt: String,
    @SerializedName("savingGoalId") val savingGoalId: Long
)
