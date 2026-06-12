package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class InvitationResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("token") val token: String,
    @SerializedName("status") val status: String,
    @SerializedName("sentAt") val sentAt: String,
    @SerializedName("expiresAt") val expiresAt: String,
    @SerializedName("respondedAt") val respondedAt: String?,
    @SerializedName("invitedBy") val invitedBy: Long,
    @SerializedName("familyId") val familyId: Long,
    @SerializedName("userInvitedId") val userInvitedId: Long,
    @SerializedName("isExpired") val isExpired: Boolean
)
