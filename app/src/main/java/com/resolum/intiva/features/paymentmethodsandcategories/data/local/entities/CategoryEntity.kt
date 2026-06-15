package com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [
        Index(value = ["ownerType", "ownerId", "type"]),
        Index(value = ["isActive"])
    ]
)
data class CategoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val ownerType: String,
    val ownerId: Long,
    val groupId: Long?,
    val type: String,
    val description: String,
    val color: String,
    val icon: String,
    val isActive: Boolean
)
