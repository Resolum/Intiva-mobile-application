package com.resolum.intiva.features.finances.domain.models

/**
 * Supported owner scopes for spending limits.
 */
object SpendingLimitOwnerType {
    const val INDIVIDUAL = "INDIVIDUAL"
    const val FAMILY = "FAMILY"

    fun isSupported(ownerType: String): Boolean =
        ownerType.equals(INDIVIDUAL, ignoreCase = true) ||
            ownerType.equals(FAMILY, ignoreCase = true)
}
