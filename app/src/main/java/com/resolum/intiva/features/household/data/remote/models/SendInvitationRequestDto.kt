package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class SendInvitationRequestDto(
    @SerializedName("userInvitedId") val userInvitedId: Long?
)
