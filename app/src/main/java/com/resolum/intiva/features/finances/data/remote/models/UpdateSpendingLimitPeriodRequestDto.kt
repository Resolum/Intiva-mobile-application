package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Request body for updating a spending limit period.
 *
 * PATCH /api/v1/spending-limits/{spendingLimitId}/period
 */
data class UpdateSpendingLimitPeriodRequestDto(
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String
)
