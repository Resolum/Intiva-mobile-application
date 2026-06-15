package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class FamilyMemberResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("familyId") val familyId: Long,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: String,
    @SerializedName("joinedAt") val joinedAt: String
)
