package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for creating a saving goal.
 *
 * POST /api/v1/saving-goals
 */
data class CreateSavingGoalRequestDto(
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("actorUserId") val actorUserId: Long,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("title") val title: String,
    @SerializedName("targetAmount") val targetAmount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("deadline") val deadline: String,
    @SerializedName("categoryId") val categoryId: Long
)
