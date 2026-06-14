package com.resolum.intiva.features.finances.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["ownerId"]),
        Index(value = ["transactionType"]),
        Index(value = ["registeredAt"])
    ]
)
data class TransactionWithDesignEntity(
    @PrimaryKey
    val id: Long,
    val amount: String,
    val currencyCode: String,
    val description: String,
    val ownerId: Long,
    val financialAccountId: Long,
    val actorUserId: Long,
    val transactionType: String,
    val categoryId: Long?,
    val registeredAt: String,
    val categoryColor: String?,
    val categoryIcon: String?
)
