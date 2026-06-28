package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

import com.google.gson.annotations.SerializedName

data class UpdateFinancialAccountRequestDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("isActive") val isActive: Boolean? = null
)
