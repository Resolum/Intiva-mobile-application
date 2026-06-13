package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class FamilyResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("ownerId") val ownerId: Long,
    @SerializedName("membersActive") val membersActive: Int,
    @SerializedName("createdAt") val createdAt: String
)
