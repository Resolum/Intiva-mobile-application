package com.resolum.intiva.features.communications.data.remote.models

import com.google.gson.annotations.SerializedName

data class NotificationDeviceResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("deviceToken") val deviceToken: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
