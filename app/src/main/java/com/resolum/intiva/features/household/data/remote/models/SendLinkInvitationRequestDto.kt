package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class SendLinkInvitationRequestDto(
    @SerializedName("familyId") val familyId: Long,
    @SerializedName("inviteeEmail") val inviteeEmail: String
)
