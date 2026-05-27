package com.resolum.intiva.features.savings.data.remote.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing a saving goal response from the API.
 *
 * This class is used to parse the JSON response from the GET endpoint into a Kotlin object.
 * It maps directly to the JSON fields returned by:
 * GET /api/v1/users/{userId}/saving-goals/{savingGoalId}
 */
data class SavingGoalResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("actorUserId") val actorUserId: Long,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("title") val title: String,
    @SerializedName("currentAmount") val currentAmount: BigDecimal,
    @SerializedName("targetAmount") val targetAmount: BigDecimal,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("description") val description: String,
    @SerializedName("startsAt") val startsAt: String?,
    @SerializedName("deadline") val deadline: String,
    @SerializedName("daysRemaining") val daysRemaining: Int,
    @SerializedName("status") val status: String,
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("completedAt") val completedAt: String?
)
