package com.resolum.intiva.features.paymentmethodsandcategories.data.local.mappers

import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.FinancialAccountEntity
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.FinancialAccountResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

fun FinancialAccountResponseDto.toEntity(userId: Long): FinancialAccountEntity {
    return FinancialAccountEntity(
        id = id,
        userId = userId,
        name = name,
        accountType = accountType,
        currencyCode = currencyCode,
        currentAmount = currentAmount,
        institution = institution,
        creditLimit = creditLimit,
        isActive = isActive ?: status.toIsActive()
    )
}

fun FinancialAccount.toEntity(userId: Long): FinancialAccountEntity {
    return FinancialAccountEntity(
        id = id,
        userId = userId,
        name = name,
        accountType = accountType,
        currencyCode = currencyCode,
        currentAmount = currentAmount,
        institution = institution,
        creditLimit = creditLimit,
        isActive = isActive
    )
}

fun FinancialAccountEntity.toDomain(): FinancialAccount {
    return FinancialAccount(
        id = id,
        name = name,
        accountType = accountType,
        currencyCode = currencyCode,
        currentAmount = currentAmount,
        institution = institution,
        creditLimit = creditLimit,
        isActive = isActive
    )
}

fun List<FinancialAccountResponseDto>.toEntities(userId: Long): List<FinancialAccountEntity> {
    return map { it.toEntity(userId) }
}

fun List<FinancialAccountEntity>.toDomainFinancialAccounts(): List<FinancialAccount> {
    return map { it.toDomain() }
}

private fun String?.toIsActive(): Boolean {
    return when {
        isNullOrBlank() -> true
        equals("ACTIVE", ignoreCase = true) -> true
        equals("ENABLED", ignoreCase = true) -> true
        equals("INACTIVE", ignoreCase = true) -> false
        equals("DISABLED", ignoreCase = true) -> false
        else -> true
    }
}
