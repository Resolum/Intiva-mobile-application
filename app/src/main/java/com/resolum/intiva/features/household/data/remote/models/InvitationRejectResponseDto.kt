package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class InvitationRejectResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("status") val status: String,
    @SerializedName("invitedByName") val invitedByName: String
)
