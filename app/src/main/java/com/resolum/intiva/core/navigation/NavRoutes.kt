package com.resolum.intiva.core.navigation

/**
 * Object containing constants for navigation routes used in the Intiva application.
 */
object NavRoutes {
    const val HOME = "home"
    const val TRANSACTIONS = "transactions"
    const val SAVINGS_GOALS = "savings_goals"
    const val FAMILY = "family"
    const val PROFILE = "profile"

    val BOTTOM_NAV_ROUTES = setOf(HOME, TRANSACTIONS, SAVINGS_GOALS, FAMILY, PROFILE)
}