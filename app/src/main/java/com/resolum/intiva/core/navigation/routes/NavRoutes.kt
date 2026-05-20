package com.resolum.intiva.core.navigation.routes

/**
 * Object containing constants for navigation routes used throughout the application. This centralizes route definitions, making it easier to manage and avoid typos.
 */
object NavRoutes {
    const val HOME                = "home"
    const val TRANSACTIONS        = "transactions"
    const val SAVINGS_GOALS       = "savings_goals"
    const val SAVINGS_GOAL_CREATE = "savings_goal_create"
    const val SAVINGS_GOAL_EDIT   = "savings_goal_edit/{id}"
    const val SAVINGS_GOAL_DETAIL = "savings_goal_detail/{id}"
    const val FAMILY              = "family"
    const val PROFILE             = "profile"

    val BOTTOM_NAV_ROUTES = setOf(HOME, TRANSACTIONS, SAVINGS_GOALS, FAMILY, PROFILE)
}