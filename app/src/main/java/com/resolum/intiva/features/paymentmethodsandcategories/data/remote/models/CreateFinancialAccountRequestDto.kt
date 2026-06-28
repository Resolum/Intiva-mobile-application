package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateFinancialAccountRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("accountType") val accountType: String,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("initialAmount") val currentAmount: Double,
    @SerializedName("institution") val institution: String?,
    @SerializedName("creditLimit") val creditLimit: Double?,
    @SerializedName("isActive") val isActive: Boolean = true
)