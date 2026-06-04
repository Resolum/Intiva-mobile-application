package com.resolum.intiva.features.finances.domain.models

/**
 * Domain request for updating a spending limit period.
 */
data class UpdateSpendingLimitPeriodRequest(
    val startDate: String,
    val endDate: String
)
