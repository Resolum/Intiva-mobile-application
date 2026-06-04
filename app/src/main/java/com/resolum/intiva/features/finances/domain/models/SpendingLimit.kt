package com.resolum.intiva.features.finances.domain.models

import java.math.BigDecimal

/**
 * Domain model representing a spending limit and its current consumption.
 */
data class SpendingLimit(
    val id: Long,
    val ownerId: Long,
    val ownerType: String,
    val targetType: String,
    val targetId: Long,
    val limitAmount: BigDecimal,
    val spentAmount: BigDecimal,
    val currencyCode: String,
    val startDate: String,
    val endDate: String,
    val active: Boolean,
    val status: String
)
