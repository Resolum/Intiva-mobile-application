package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class DeferredInvitationResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("groupName") val groupName: String?,
    @SerializedName("inviterName") val inviterName: String?
)
