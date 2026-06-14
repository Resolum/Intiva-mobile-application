package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for updating a spending limit amount.
 *
 * PATCH /api/v1/spending-limits/{spendingLimitId}/amount
 */
data class UpdateSpendingLimitAmountRequestDto(
    @SerializedName("limitAmount") val limitAmount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String
)
