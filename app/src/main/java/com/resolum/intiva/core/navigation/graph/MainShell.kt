package com.resolum.intiva.core.navigation.graph

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.navigation.components.IntivaBottomNavBar
import com.resolum.intiva.core.navigation.routes.NavRoutes
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.presentation.HomeScreen
import com.resolum.intiva.features.finances.presentation.spendinglimits.SpendingLimitScreen
import com.resolum.intiva.features.finances.presentation.transactions.TransactionFormScreen
import com.resolum.intiva.features.finances.presentation.transactions.TransactionsScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalCreateScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalDetailScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalEditScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalsScreen
import com.resolum.intiva.features.savings.presentation.completion.GoalCompletedScreen
import com.resolum.intiva.features.savings.presentation.completion.GoalUncompletedScreen
import com.resolum.intiva.features.savings.presentation.contribute.ContributeToGoalScreen
import com.resolum.intiva.features.shared.domain.model.OwnerType

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
        containerColor = Color.White,
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
                    onNavigateToSpendingLimitAlert = {
                        shellNavController.navigate(NavRoutes.SPENDING_LIMIT_ALERT)
                    },
                )
            }

            /**
             * Placeholder destinations for features that haven't been implemented yet.
             * These can be fleshed out with actual screens and nested navigation graphs as the app is developed.
             */
            composable(NavRoutes.TRANSACTIONS) {
                TransactionsScreen()
            }
            composable(NavRoutes.SPENDING_LIMIT_ALERT) {
                SpendingLimitScreen(onNavigateBack = { shellNavController.popBackStack() })
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

            /**
             * The create screen is shown when a user wants to create a new savings goal. It requires the account ID to know which account the goal belongs to.
             * After successfully creating a goal, the user is navigated to the detail screen for that goal.
             */
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
                    onGoalUpdated = { shellNavController.popBackStack() },
                    onGoalCompleted = { goalId ->
                        shellNavController.navigate("savings_goal_completed/0/$goalId") {
                            popUpTo(NavRoutes.SAVINGS_GOALS)
                        }
                    }
                )
            }
            /**
             * The detail screen allows users to view the details of a specific savings goal.
             * It allows them to enter an amount and confirm the contribution.
             * If the contribution is successful, they are navigated to the completion screen. If it fails (e.g. due to insufficient funds), they are navigated to the uncompleted screen.
             */
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

            /**
             * The contribute screen allows users to add funds to a specific savings goal. It requires both the account ID and goal ID to know which goal to contribute to.
             * After contributing, users can either be taken back to the goal detail screen or shown a completion/uncompletion screen based on whether the contribution completed the goal or not.
             */
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

            /**
             * The completed screen is shown when a user successfully completes a savings goal. It provides options to create a new goal or go back to the goals list.
             * We pass the accountId and goalId as parameters to this screen so it can display relevant information about the completed goal.
             */
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
                        shellNavController.navigate(NavRoutes.HOME) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            /**
             * The uncompleted screen is shown when a user tries to contribute to a goal but doesn't have enough funds.
             * It provides options to try contributing again (which will take them back to the contribute screen) or to go back to the savings goals list.
             */
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

            /**
             * Transaction form screens for creating new income or expense transactions.
             * These are registered at the root level for simplicity, but in a larger app you might want to create a separate nav graph for transaction-related screens.
             */
            composable(NavRoutes.NEW_INCOME) {
                TransactionFormScreen(
                    transactionType = TransactionType.INCOME,
                    onDismiss = { shellNavController.popBackStack() },
                    navController = shellNavController,
                    ownerType = OwnerType.INDIVIDUAL
                )
            }

            /**
             * The new expense screen is very similar to the new income screen, but with the transaction type set to EXPENSE.
             * We reuse the same TransactionFormScreen composable for both, passing in the appropriate transaction type as a parameter.
             */
            composable(NavRoutes.NEW_EXPENSE) {
                TransactionFormScreen(
                    transactionType = TransactionType.EXPENSE,
                    onDismiss = { shellNavController.popBackStack() },
                    navController = shellNavController,
                    ownerType = OwnerType.INDIVIDUAL
                )
            }
        }
    }
}
