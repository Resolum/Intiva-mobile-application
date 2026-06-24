package com.resolum.intiva.features.paymentmethodsandcategories.domain.models

data class FinancialAccount(
    val id: Long,
    val name: String,
    val accountType: String,
    val currencyCode: String,
    val currentAmount: Double,
    val institution: String?,
    val creditLimit: Double?,
    val isActive: Boolean,
    val version: Long = 0
)
