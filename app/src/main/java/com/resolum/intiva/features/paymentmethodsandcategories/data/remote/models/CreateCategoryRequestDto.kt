package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateCategoryRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("ownerType") val ownerType: String = "USER",
    @SerializedName("description") val description: String = "",
    @SerializedName("color") val color: String = "#534AB7",
    @SerializedName("icon") val icon: String,
    @SerializedName("isActive") val isActive: Boolean = true
)