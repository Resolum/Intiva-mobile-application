package com.resolum.intiva.features.paymentmethodsandcategories.domain.models

/**
 * Data class representing a financial category, which can be associated with payment methods and financial accounts.
 *
 * @property id Unique identifier for the category.
 * @property name Name of the category.
 * @property ownerType Type of the owner (e.g., "user", "group").
 * @property ownerId ID of the user who owns the category.
 * @property groupId Optional ID of the group associated with the category, if applicable.
 * @property isActive Indicates whether the category is active or not.
 * @property description Description of the category.
 * @property color Color code associated with the category for UI representation.
 * @property icon Icon name or URL associated with the category for UI representation.
 */
data class Category(
    val id: Long,
    val name: String,
    val ownerType: String,
    val ownerId: Long,
    val groupId: Long?,
    val isActive: Boolean,
    val description: String,
    val color: String,
    val icon: String
)
