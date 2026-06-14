package com.resolum.intiva.features.finances.data.local.mappers

import com.resolum.intiva.features.finances.data.local.entities.TransactionWithDesignEntity
import com.resolum.intiva.features.finances.data.remote.models.TransactionWithDesignResponseDto
import com.resolum.intiva.features.finances.domain.models.CategoryDesign
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.domain.models.TransactionWithDesignResponse

fun TransactionWithDesignResponseDto.toEntity(): TransactionWithDesignEntity {
    return TransactionWithDesignEntity(
        id = id,
        amount = amount,
        currencyCode = currencyCode,
        description = description,
        ownerId = ownerId,
        financialAccountId = financialAccountId,
        actorUserId = actorUserId,
        transactionType = transactionType,
        categoryId = categoryId,
        registeredAt = registeredAt,
        categoryColor = categoryDesign.categoryColor,
        categoryIcon = categoryDesign.categoryIcon
    )
}

fun TransactionWithDesignResponse.toEntity(): TransactionWithDesignEntity {
    return TransactionWithDesignEntity(
        id = id,
        amount = amount,
        currencyCode = currencyCode,
        description = description,
        ownerId = ownerId,
        financialAccountId = financialAccountId,
        actorUserId = actorUserId,
        transactionType = transactionType,
        categoryId = categoryId,
        registeredAt = registeredAt,
        categoryColor = categoryDesign?.categoryColor,
        categoryIcon = categoryDesign?.categoryIcon
    )
}

fun TransactionWithDesignEntity.toDomain(): TransactionWithDesignResponse {
    return TransactionWithDesignResponse(
        id = id,
        amount = amount,
        currencyCode = currencyCode,
        description = description,
        ownerId = ownerId,
        financialAccountId = financialAccountId,
        actorUserId = actorUserId,
        transactionType = transactionType,
        categoryId = categoryId,
        registeredAt = registeredAt,
        categoryDesign = if (categoryColor != null && categoryIcon != null) {
            CategoryDesign(
                categoryColor = categoryColor,
                categoryIcon = categoryIcon
            )
        } else {
            null
        }
    )
}

fun TransactionWithDesignEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        currencyCode = currencyCode,
        description = description,
        ownerId = ownerId,
        financialAccountId = financialAccountId,
        actorUserId = actorUserId,
        transactionType = transactionType,
        categoryId = categoryId,
        registeredAt = registeredAt
    )
}

fun List<TransactionWithDesignEntity>.toTransactionGroups(): List<TransactionGroupByDate> {
    return map { it.toDomain() }
        .groupBy { it.registeredAt.take(10) }
        .map { (date, transactions) ->
            TransactionGroupByDate(
                date = date,
                transactions = transactions.sortedByDescending { it.registeredAt }
            )
        }
        .sortedByDescending { it.date }
}

fun List<TransactionGroupByDate>.toEntities(): List<TransactionWithDesignEntity> {
    return flatMap { group -> group.transactions.map { it.toEntity() } }
}
