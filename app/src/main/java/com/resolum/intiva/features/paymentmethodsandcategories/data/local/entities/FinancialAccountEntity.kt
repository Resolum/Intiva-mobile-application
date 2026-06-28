package com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "financial_accounts",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["isActive"])
    ]
)
data class FinancialAccountEntity(
    @PrimaryKey
    val id: Long,
    val userId: Long,
    val name: String,
    val accountType: String,
    val currencyCode: String,
    val currentAmount: Double,
    val institution: String?,
    val creditLimit: Double?,
    val isActive: Boolean,
    val version: Long = 0
)
