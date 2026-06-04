package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for creating a spending limit.
 *
 * POST /api/v1/spending-limits
 */
data class CreateSpendingLimitRequestDto(
    @SerializedName("ownerId") val ownerId: Long,
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("targetType") val targetType: String,
    @SerializedName("targetId") val targetId: Long,
    @SerializedName("limitAmount") val limitAmount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String
)
