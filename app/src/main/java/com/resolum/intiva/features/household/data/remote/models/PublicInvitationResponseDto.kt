package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class PublicInvitationResponseDto(
    @SerializedName("invitationId") val id: Long? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("familyId") val familyId: Long? = null,
    @SerializedName("invitedByName") val invitedByName: String? = null,
    @SerializedName("groupName") val groupName: String? = null,
    @SerializedName("isExpired") val isExpired: Boolean? = null
)
