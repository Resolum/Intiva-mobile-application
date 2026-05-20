package com.resolum.intiva.core.navigation.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Navigates to [route] clearing the back stack up to [popUpTo] (inclusive by default).
 * Useful for login → home transitions where you don't want the user to go back.
 */
fun NavController.navigateAndClearBackStack(
    route: String,
    popUpTo: String = graph.startDestinationRoute ?: route,
) {
    navigate(route) {
        popUpTo(popUpTo) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Navigates to [route] with [launchSingleTop] = true, avoiding duplicate back-stack entries.
 */
fun NavController.navigateSingleTop(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(route) {
        launchSingleTop = true
        builder()
    }
}
