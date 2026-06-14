package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a financial account response from the API.
 *
 * This class is used to parse the JSON response from the API into a Kotlin object. It contains
 * all the necessary fields that are expected in the financial account response, such as id, name,
 * accountType, currencyCode, currentAmount, institution, creditLimit, and isActive.
 */
data class FinancialAccountResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("accountType") val accountType: String,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("currentAmount") val currentAmount: Double,
    @SerializedName("institution") val institution: String?,
    @SerializedName("creditLimit") val creditLimit: Double?,
    @SerializedName(value = "isActive", alternate = ["active", "enabled"]) val isActive: Boolean? = null,
    @SerializedName("status") val status: String? = null
)
