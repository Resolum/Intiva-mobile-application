package com.resolum.intiva.features.finances.domain.models

import java.math.BigDecimal

/**
 * Domain request for updating a spending limit amount.
 */
data class UpdateSpendingLimitAmountRequest(
    val limitAmount: BigDecimal,
    val currencyCode: String
)
