package com.resolum.intiva.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.resolum.intiva.features.finances.presentation.HomeScreen
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
    startDestination: String = Screen.SignIn.route,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
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
         * Finances Screens
         */
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}
