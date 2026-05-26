package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.mappers

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.FinancialAccountResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

/**
 * Extension function to map a [FinancialAccountResponseDto] to a [FinancialAccount] domain model.
 *
 * This function takes the properties from the response DTO and constructs a corresponding
 * domain model instance, which can be used throughout the application for business logic and UI representation.
 */
fun FinancialAccountResponseDto.toDomain() = FinancialAccount(
    id = id,
    name = name,
    accountType = accountType,
    currencyCode = currencyCode,
    currentAmount = currentAmount,
    institution = institution,
    creditLimit = creditLimit,
    isActive = isActive
)