package com.resolum.intiva.features.communications.data.remote.models

import com.google.gson.annotations.SerializedName

data class InAppNotificationResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<InAppNotificationDto>
)
