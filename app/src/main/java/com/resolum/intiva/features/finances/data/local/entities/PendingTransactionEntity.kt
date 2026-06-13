package com.resolum.intiva.features.finances.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.resolum.intiva.features.finances.domain.models.AccountType
import com.resolum.intiva.features.finances.domain.models.SyncStatus

@Entity(tableName = "pending_transactions")
data class PendingTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val amount: String,
    val currencyCode: String,
    val categoryId: Long,
    val financialAccountId: Long,
    val accountType: AccountType,
    val transactionType: String,
    val ownerType: String,
    val date: Long,
    val note: String,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val conflictReason: String? = null
)
