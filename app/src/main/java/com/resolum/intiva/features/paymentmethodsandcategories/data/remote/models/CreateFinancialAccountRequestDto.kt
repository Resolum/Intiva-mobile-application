package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

data class CreateFinancialAccountRequestDto(
    val name: String,
    val accountType: String,
    val currencyCode: String,
    val currentAmount: Double,
    val institution: String?,
    val creditLimit: Double?,
    val isActive: Boolean = true
)