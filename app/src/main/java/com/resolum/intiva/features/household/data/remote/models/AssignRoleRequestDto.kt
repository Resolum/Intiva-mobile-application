package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class AssignRoleRequestDto(
    @SerializedName("role") val role: String
)
