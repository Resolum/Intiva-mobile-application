package com.resolum.intiva.features.paymentmethodsandcategories.domain.models

/**
 * Data class representing a financial account, which can be associated with payment methods and categories.
 *
 * @property id Unique identifier for the financial account.
 * @property name Name of the financial account.
 * @property accountType Type of the financial account (e.g., "checking", "savings", "credit").
 * @property currencyCode ISO currency code associated with the financial account (e.g., "USD", "EUR").
 * @property currentAmount Current balance or amount in the financial account.
 * @property institution Optional name of the financial institution associated with the account.
 * @property creditLimit Optional credit limit for credit accounts, if applicable.
 * @property isActive Indicates whether the financial account is active or not.
 */
data class FinancialAccount(
    val id: Long,
    val name: String,
    val accountType: String,
    val currencyCode: String,
    val currentAmount: Double,
    val institution: String?,
    val creditLimit: Double?,
    val isActive: Boolean
)
