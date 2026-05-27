package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for creating a saving goal.
 *
 * POST /api/v1/users/{userId}/saving-goals
 */
data class CreateSavingGoalRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("targetAmount") val targetAmount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("deadline") val deadline: String,
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("description") val description: String = ""
)
