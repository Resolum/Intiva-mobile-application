package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class QrCodeResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("qrBase64") val qrBase64: String,
    @SerializedName("invitationLink") val invitationLink: String,
    @SerializedName("expiresAt") val expiresAt: String
)
