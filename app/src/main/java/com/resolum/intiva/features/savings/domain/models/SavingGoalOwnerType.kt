package com.resolum.intiva.features.savings.domain.models

/**
 * Known owner types returned by the savings goals API.
 */
object SavingGoalOwnerType {
    const val INDIVIDUAL = "INDIVIDUAL"
    const val USER = "USER"
    const val FAMILY = "FAMILY"
    const val GROUP = "GROUP"

    fun isPersonal(ownerType: String): Boolean =
        ownerType.equals(INDIVIDUAL, ignoreCase = true) ||
            ownerType.equals(USER, ignoreCase = true) ||
            ownerType.equals("PERSONAL", ignoreCase = true)

    fun isFamily(ownerType: String): Boolean =
        ownerType.equals(FAMILY, ignoreCase = true) ||
            ownerType.equals(GROUP, ignoreCase = true)
}
