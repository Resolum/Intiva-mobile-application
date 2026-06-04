package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for updating a saving goal.
 *
 * Only non-null fields are sent; null fields are omitted from the JSON.
 */
data class UpdateSavingGoalRequestDto(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("newTargetAmount") val newTargetAmount: BigDecimal?
)
