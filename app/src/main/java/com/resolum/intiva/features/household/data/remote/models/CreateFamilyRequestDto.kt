package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateFamilyRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)
