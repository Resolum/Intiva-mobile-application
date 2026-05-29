package com.resolum.intiva.features.savings.domain.models

import java.math.BigDecimal

/**
 * Represents an amount of money in a specific currency.
 *
 * @property amount The numeric value of the money.
 * @property currencyCode The ISO 4217 currency code (e.g., "USD", "PEN").
 */
data class Money(
    val amount: BigDecimal,
    val currencyCode: String
)
