package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a spending limit returned by the API.
 */
data class SpendingLimitResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ownerId") val ownerId: Long,
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("targetType") val targetType: String,
    @SerializedName("targetId") val targetId: Long,
    @SerializedName("limitAmount") val limitAmount: String,
    @SerializedName("spentAmount") val spentAmount: String,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("status") val status: String
)
