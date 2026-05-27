package com.resolum.intiva.core.navigation.routes

/**
 * Object containing constants for navigation routes used throughout the application. This centralizes route definitions, making it easier to manage and avoid typos.
 */
object NavRoutes {
    const val HOME                        = "home"
    const val TRANSACTIONS                = "transactions"
    const val SAVINGS_GOALS               = "savings_goals"
    const val SAVINGS_GOAL_CREATE         = "savings_goal_create/{accountId}"
    const val SAVINGS_GOAL_EDIT           = "savings_goal_edit/{id}"
    const val SAVINGS_GOAL_DETAIL         = "savings_goal_detail/{accountId}/{goalId}"
    const val SAVINGS_GOAL_CONTRIBUTE     = "savings_goal_contribute/{accountId}/{goalId}"
    const val SAVINGS_GOAL_COMPLETED      = "savings_goal_completed/{accountId}/{goalId}"
    const val SAVINGS_GOAL_UNCOMPLETED    = "savings_goal_uncompleted/{accountId}/{goalId}"
    const val FAMILY                      = "family"
    const val PROFILE                     = "profile"

    const val NEW_INCOME           = "transactions/new_income"
    const val NEW_EXPENSE          = "transaction/new_expense"

    val BOTTOM_NAV_ROUTES = setOf(HOME, TRANSACTIONS, SAVINGS_GOALS, FAMILY, PROFILE)
}