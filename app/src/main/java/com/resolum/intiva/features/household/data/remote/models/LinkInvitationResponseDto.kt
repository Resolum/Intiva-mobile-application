package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class LinkInvitationResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("inviteUrl") val inviteUrl: String,
    @SerializedName("expiresAt") val expiresAt: String
)
