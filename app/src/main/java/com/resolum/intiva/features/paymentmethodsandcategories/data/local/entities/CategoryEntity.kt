package com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities

import androidx.room.Entity

@Entity(tableName = "categories")
data class CategoryEntity(
    val id: String,
    val name: String,
    val ownerType: String,
    val type: String,
    val icon: String,
    val color: String,
    val isActive: Boolean
)