package com.resolum.intiva.features.communications.data.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterNotificationDeviceRequestDto(
    @SerializedName("userId") val userId: Long,
    @SerializedName("deviceToken") val deviceToken: String,
    @SerializedName("platform") val platform: String
)
