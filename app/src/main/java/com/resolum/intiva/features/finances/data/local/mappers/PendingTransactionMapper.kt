package com.resolum.intiva.features.finances.data.local.mappers

import com.resolum.intiva.features.finances.data.local.entities.PendingTransactionEntity
import com.resolum.intiva.features.finances.data.remote.models.RegisterTransactionRequestDto
import com.resolum.intiva.features.finances.domain.models.AccountType
import com.resolum.intiva.features.finances.domain.models.RegisterTransactionRequest
import com.resolum.intiva.features.finances.domain.models.Transaction
import java.util.UUID

fun RegisterTransactionRequest.toPendingEntity(
    userId: Long,
    accountType: AccountType,
    date: Long = System.currentTimeMillis()
): PendingTransactionEntity {
    return PendingTransactionEntity(
        userId = userId,
        amount = amount.toPlainString(),
        currencyCode = currencyCode,
        categoryId = categoryId,
        financialAccountId = financialAccountId,
        accountType = accountType,
        transactionType = transactionType.name,
        ownerType = ownerType,
        date = date,
        note = description,
        idempotencyKey = UUID.randomUUID().toString()
    )
}

fun PendingTransactionEntity.toRegisterTransactionRequestDto(): RegisterTransactionRequestDto {
    return RegisterTransactionRequestDto(
        amount = amount.toBigDecimal(),
        currencyCode = currencyCode,
        description = note,
        financialAccountId = financialAccountId,
        userId = userId,
        performedByUserId = userId,
        transactionType = transactionType,
        categoryId = categoryId,
        ownerType = ownerType
    )
}

fun PendingTransactionEntity.toDomainTransaction(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        currencyCode = currencyCode,
        description = note,
        ownerId = userId,
        financialAccountId = financialAccountId,
        actorUserId = userId,
        transactionType = transactionType,
        categoryId = categoryId,
        registeredAt = date.toString()
    )
}

fun String.toAccountType(): AccountType {
    return when {
        equals("FAMILY", ignoreCase = true) -> AccountType.FAMILY
        equals("GROUP", ignoreCase = true) -> AccountType.FAMILY
        else -> AccountType.PERSONAL
    }
}
