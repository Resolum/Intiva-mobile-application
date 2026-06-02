package com.resolum.intiva.features.wallet.domain.models

data class FinancialAccount(
    val id: String,
    val bankName: String,
    val maskedNumber: String,
    val balance: Double,
    val isActive: Boolean,
    val accountType: AccountType
)

enum class AccountType {
    BANK,
    WALLET
}