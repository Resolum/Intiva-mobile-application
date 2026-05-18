package com.resolum.intiva.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.resolum.intiva.core.ui.components.IntivaBottomNavBar
import com.resolum.intiva.features.home.presentation.home.HomeView
import com.resolum.intiva.features.iam.presentation.signin.SignInScreen
import com.resolum.intiva.features.iam.presentation.signup.SignUpScreen

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
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Home   : Screen("home")

    data object SavingsGoals : Screen("savings_goals")
    data object SavingsGoalCreate : Screen("savings_goal_create")
    data object SavingsGoalEdit : Screen("savings_goal_edit/{id}") {
        fun createRoute(id: String) = "savings_goal_edit/$id"
    }
    data object SavingsGoalDetail : Screen("savings_goal_detail/{id}") {
        fun createRoute(id: String) = "savings_goal_detail/$id"
    }
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
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            IntivaBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            /**
             * IAM Screens
             */
    composable(Screen.SignIn.route) {
                SignInScreen(
                    onSignInSuccess = {
                        navController.navigateAndClearBackStack(
                            route = Screen.Home.route,
                            popUpTo = Screen.SignIn.route,
                        )
                    },
                    onNavigateToSignUp = {
                        navController.navigateSingleTop(Screen.SignUp.route)
                    },
                    onForgotPassword = { /* TODO: Add Route */ },
                    onGoogleSignIn   = { /* TODO: Add Google Sign In */ },
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigateAndClearBackStack(
                            route = Screen.Home.route,
                            popUpTo = Screen.SignUp.route,
                        )
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        navController.navigateAndClearBackStack(
                            route = Screen.SignIn.route,
                            popUpTo = Screen.SignUp.route,
                        )
                    },
                )
            }

            /**
             * Home View
             */
            composable(Screen.Home.route) {
                HomeView()
            }

            /**
             * Savings Screens
             */
            composable(Screen.SavingsGoals.route) {
                com.resolum.intiva.features.savings.presentation.SavingsGoalsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCreate = { navController.navigate(Screen.SavingsGoalCreate.route) },
                    onNavigateToDetail = { id -> navController.navigate(Screen.SavingsGoalDetail.createRoute(id)) },
                    onNavigateToEdit = { id -> 
                        navController.navigate(Screen.SavingsGoalEdit.createRoute(id)) 
                    }
                )
            }
            
            composable(Screen.SavingsGoalCreate.route) {
                com.resolum.intiva.features.savings.presentation.SavingsGoalCreateScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onGoalCreated = { navController.popBackStack() }
                )
            }
            
            composable(Screen.SavingsGoalEdit.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                com.resolum.intiva.features.savings.presentation.SavingsGoalEditScreen(
                    goalId = id,
                    onNavigateBack = { navController.popBackStack() },
                    onGoalUpdated = { navController.popBackStack() }
                )
            }
            
            composable(Screen.SavingsGoalDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                com.resolum.intiva.features.savings.presentation.SavingsGoalDetailScreen(
                    goalId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
