package com.resolum.intiva.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

/**
 * Sealed class that centralises all navigation routes in the app.
 *
 * Each feature module should add its routes here (or define its own sealed subclass
 * and register them in [AppNavGraph]).
 *
 * Example:
 * ```
 * sealed class Screen(val route: String) {
 *     data object Home : Screen("home")
 *     data object Detail : Screen("detail/{id}") {
 *         fun createRoute(id: Int) = "detail/$id"
 *     }
 * }
 * ```
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
}

/**
 * Root nav graph. Register each feature's composable destinations inside the [NavHost] block.
 *
 * @param navController The app-level [NavHostController].
 * @param startDestination The initial destination shown on launch.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
    }
}
