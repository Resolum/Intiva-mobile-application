package com.resolum.intiva.features.communications.data.remote.models

import com.google.gson.annotations.SerializedName

data class InAppNotificationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("recipientUserId") val recipientUserId: Long,
    @SerializedName("type") val type: String,
    @SerializedName("source") val source: String,
    @SerializedName("sourceId") val sourceId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String
)
