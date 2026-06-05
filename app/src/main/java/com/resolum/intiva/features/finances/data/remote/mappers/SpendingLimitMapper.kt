package com.resolum.intiva.features.finances.data.remote.mappers

import com.resolum.intiva.features.finances.data.remote.models.CreateSpendingLimitRequestDto
import com.resolum.intiva.features.finances.data.remote.models.SpendingLimitResponseDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitAmountRequestDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitPeriodRequestDto
import com.resolum.intiva.features.finances.domain.models.CreateSpendingLimitRequest
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitAmountRequest
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitPeriodRequest
import java.math.BigDecimal

fun CreateSpendingLimitRequest.toDto(): CreateSpendingLimitRequestDto =
    CreateSpendingLimitRequestDto(
        ownerId = ownerId,
        ownerType = ownerType,
        targetType = targetType,
        targetId = targetId,
        limitAmount = limitAmount,
        currencyCode = currencyCode,
        startDate = startDate,
        endDate = endDate
    )

fun UpdateSpendingLimitAmountRequest.toDto(): UpdateSpendingLimitAmountRequestDto =
    UpdateSpendingLimitAmountRequestDto(
        limitAmount = limitAmount,
        currencyCode = currencyCode
    )

fun UpdateSpendingLimitPeriodRequest.toDto(): UpdateSpendingLimitPeriodRequestDto =
    UpdateSpendingLimitPeriodRequestDto(
        startDate = startDate,
        endDate = endDate
    )

fun SpendingLimitResponseDto.toDomain(): SpendingLimit =
    SpendingLimit(
        id = id,
        ownerId = ownerId,
        ownerType = ownerType,
        targetType = targetType,
        targetId = targetId,
        limitAmount = limitAmount.toBigDecimalOrZero(),
        spentAmount = spentAmount.toBigDecimalOrZero(),
        currencyCode = currencyCode,
        startDate = startDate,
        endDate = endDate,
        active = active,
        status = status
    )

private fun String.toBigDecimalOrZero(): BigDecimal =
    toBigDecimalOrNull() ?: BigDecimal.ZERO
