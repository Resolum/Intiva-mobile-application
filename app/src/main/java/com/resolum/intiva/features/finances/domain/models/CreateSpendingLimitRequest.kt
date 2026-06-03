package com.resolum.intiva.features.finances.domain.models

import java.math.BigDecimal

/**
 * Domain request for creating a spending limit.
 */
data class CreateSpendingLimitRequest(
    val ownerId: Long,
    val ownerType: String,
    val targetType: String,
    val targetId: Long,
    val limitAmount: BigDecimal,
    val currencyCode: String,
    val startDate: String,
    val endDate: String
)
