package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing the request body for creating a contribution.
 *
 * Used as the body for:
 * POST /api/v1/accounts/{accountId}/saving-goals/{savingGoalId}/contributions
 */
data class ContributionRequestDto(
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("contributorId") val contributorId: Long
)
