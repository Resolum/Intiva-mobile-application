package com.resolum.intiva.core.navigation.graph

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.navigation.components.IntivaBottomNavBar
import com.resolum.intiva.core.navigation.routes.NavRoutes
import com.resolum.intiva.features.finances.presentation.TransactionsScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalCreateScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalDetailScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalEditScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalsScreen

@Composable
fun MainShell() {
    val shellNavController = rememberNavController()
    val navBackStackEntry by shellNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            IntivaBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    shellNavController.navigate(route) {
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }, contentWindowInsets = WindowInsets(0)
    ) { padding ->
        NavHost(
            navController = shellNavController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {

            composable(NavRoutes.HOME) { TransactionsScreen() }
            composable(NavRoutes.TRANSACTIONS) { }
            composable(NavRoutes.FAMILY) { }
            composable(NavRoutes.PROFILE) { }

            composable(NavRoutes.SAVINGS_GOALS) {
                SavingsGoalsScreen(
                    onNavigateBack = { shellNavController.popBackStack() },
                    onNavigateToCreate = { shellNavController.navigate(NavRoutes.SAVINGS_GOAL_CREATE) },
                    onNavigateToDetail = { id -> shellNavController.navigate("savings_goal_detail/$id") },
                    onNavigateToEdit = { id -> shellNavController.navigate("savings_goal_edit/$id") }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_CREATE) {
                SavingsGoalCreateScreen(
                    onNavigateBack = { shellNavController.popBackStack() },
                    onGoalCreated = { shellNavController.popBackStack() }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_EDIT) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                SavingsGoalEditScreen(
                    goalId = id,
                    onNavigateBack = { shellNavController.popBackStack() },
                    onGoalUpdated = { shellNavController.popBackStack() }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_DETAIL) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                SavingsGoalDetailScreen(
                    goalId = id,
                    onNavigateBack = { shellNavController.popBackStack() }
                )
            }
        }
    }
}