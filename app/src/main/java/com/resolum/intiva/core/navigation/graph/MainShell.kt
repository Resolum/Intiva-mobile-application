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
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.presentation.HomeScreen
import com.resolum.intiva.features.finances.presentation.transactions.TransactionFormScreen
import com.resolum.intiva.features.finances.presentation.transactions.TransactionsScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalCreateScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalDetailScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalEditScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalsScreen
import com.resolum.intiva.features.savings.presentation.completion.GoalCompletedScreen
import com.resolum.intiva.features.savings.presentation.completion.GoalUncompletedScreen
import com.resolum.intiva.features.savings.presentation.contribute.ContributeToGoalScreen

/**
 * Main shell of the app, containing the bottom navigation and root-level destinations.
 * Each feature's main screen should be registered here, with deeper navigation handled within the feature's own nav graph.
 */
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

            /**
             * Root-level destinations
             * Each of these should ideally just host the main screen for the feature, with deeper navigation handled in the feature's own nav graph.
             */
            composable(NavRoutes.HOME) {
                HomeScreen(
                    onNavigateToNewExpense = { shellNavController.navigate(NavRoutes.NEW_EXPENSE) },
                    onNavigateToNewIncome = { shellNavController.navigate(NavRoutes.NEW_INCOME) },
                    navController = shellNavController,
                    onNavigateToTransactions = { shellNavController.navigate(NavRoutes.TRANSACTIONS) },
                )
            }

            /**
             * Placeholder destinations for features that haven't been implemented yet.
             * These can be fleshed out with actual screens and nested navigation graphs as the app is developed.
             */
            composable(NavRoutes.TRANSACTIONS) {
                TransactionsScreen()
            }
            composable(NavRoutes.FAMILY) { }
            composable(NavRoutes.PROFILE) { }

            /**
             * Savings Goals feature navigation.
             * This is a bit more complex, with multiple screens for listing, creating, editing, and viewing details of savings goals.
             * Each screen is registered here for simplicity, but in a larger app you might want to create a separate nav graph for the savings feature.
             */
            composable(NavRoutes.SAVINGS_GOALS) {
                SavingsGoalsScreen(
                    onNavigateBack = { shellNavController.popBackStack() },
                    onNavigateToCreate = { accountId ->
                        shellNavController.navigate("savings_goal_create/$accountId")
                    },
                    onNavigateToDetail = { accountId, goalId ->
                        shellNavController.navigate("savings_goal_detail/$accountId/$goalId")
                    },
                    onNavigateToEdit = { accountId, goalId ->
                        shellNavController.navigate("savings_goal_edit/$goalId")
                    }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_CREATE) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull()
                    ?: return@composable
                SavingsGoalCreateScreen(
                    accountId = accountId,
                    onNavigateBack = { shellNavController.popBackStack() },
                    onGoalCreated = { resolvedAccountId, goalId ->
                        shellNavController.navigate("savings_goal_detail/$resolvedAccountId/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    }
                )
            }

            /**
             * The edit and detail screens require an ID parameter to know which savings goal to display or edit.
             * We extract this ID from the back stack entry's arguments and pass it to the respective screen.
             */
            composable(NavRoutes.SAVINGS_GOAL_EDIT) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                SavingsGoalEditScreen(
                    goalId = id,
                    onNavigateBack = { shellNavController.popBackStack() },
                    onGoalUpdated = { shellNavController.popBackStack() }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_DETAIL) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull() ?: return@composable
                val goalId = backStackEntry.arguments?.getString("goalId") ?: return@composable
                SavingsGoalDetailScreen(
                    accountId = accountId,
                    goalId = goalId.toLongOrNull() ?: return@composable,
                    onNavigateBack = { shellNavController.popBackStack() },
                    onContributeClick = {
                        shellNavController.navigate("savings_goal_contribute/$accountId/$goalId")
                    },
                    onGoalCompleted = {
                        shellNavController.navigate("savings_goal_completed/$accountId/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    },
                    onGoalUncompleted = {
                        shellNavController.navigate("savings_goal_uncompleted/$accountId/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_CONTRIBUTE) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull() ?: return@composable
                val goalId = backStackEntry.arguments?.getString("goalId")?.toLongOrNull() ?: return@composable
                ContributeToGoalScreen(
                    accountId = accountId,
                    goalId = goalId,
                    onNavigateBack = { shellNavController.popBackStack() },
                    onGoalCompleted = {
                        shellNavController.navigate("savings_goal_completed/$accountId/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    },
                    onGoalUncompleted = {
                        shellNavController.navigate("savings_goal_uncompleted/$accountId/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    },
                    onContributionSuccess = { _ -> shellNavController.popBackStack() }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_COMPLETED) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull() ?: return@composable
                val goalId = backStackEntry.arguments?.getString("goalId")?.toLongOrNull() ?: return@composable
                GoalCompletedScreen(
                    accountId = accountId,
                    goalId = goalId,
                    onNewGoal = {
                        shellNavController.navigate("savings_goal_create/$accountId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    },
                    onBack = {
                        shellNavController.navigate(NavRoutes.SAVINGS_GOALS) {
                            popUpTo(NavRoutes.SAVINGS_GOALS) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.SAVINGS_GOAL_UNCOMPLETED) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull() ?: return@composable
                val goalId = backStackEntry.arguments?.getString("goalId")?.toLongOrNull() ?: return@composable
                GoalUncompletedScreen(
                    accountId = accountId,
                    goalId = goalId,
                    onTryAgain = {
                        shellNavController.navigate("savings_goal_contribute/$accountId/$goalId")
                    },
                    onBack = {
                        shellNavController.navigate(NavRoutes.SAVINGS_GOALS) {
                            popUpTo(NavRoutes.SAVINGS_GOALS) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.NEW_INCOME) {
                TransactionFormScreen(
                    transactionType = TransactionType.INCOME,
                    onDismiss = { shellNavController.popBackStack() },
                    navController = shellNavController
                )
            }

            composable(NavRoutes.NEW_EXPENSE) {
                TransactionFormScreen(
                    transactionType = TransactionType.EXPENSE,
                    onDismiss = { shellNavController.popBackStack() },
                    navController = shellNavController
                )
            }
        }
    }
}