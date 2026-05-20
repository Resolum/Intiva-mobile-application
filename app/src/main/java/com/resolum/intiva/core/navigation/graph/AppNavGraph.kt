package com.resolum.intiva.core.navigation.graph

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.resolum.intiva.core.navigation.components.IntivaBottomNavBar
import com.resolum.intiva.core.navigation.extensions.navigateAndClearBackStack
import com.resolum.intiva.core.navigation.extensions.navigateSingleTop
import com.resolum.intiva.core.navigation.routes.NavRoutes
import com.resolum.intiva.core.navigation.routes.Screen
import com.resolum.intiva.features.finances.presentation.TransactionsScreen
import com.resolum.intiva.features.iam.presentation.onboarding.OnboardingScreen
import com.resolum.intiva.features.iam.presentation.signin.SignInScreen
import com.resolum.intiva.features.iam.presentation.signup.SignUpScreen
import com.resolum.intiva.features.iam.presentation.splash.SplashScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalCreateScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalDetailScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalEditScreen
import com.resolum.intiva.features.savings.presentation.SavingsGoalsScreen

/**
 * Root nav graph. Register each feature's composable destinations inside the [NavHost] block.
 *
 * @param navController The app-level [NavHostController].
 * @param startDestination The initial destination shown on launch.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigateAndClearBackStack(
                        route = Screen.Onboarding.route,
                        popUpTo = Screen.Splash.route,
                    )
                },
                onNavigateToSignIn = {
                    navController.navigateAndClearBackStack(
                        route = Screen.SignIn.route,
                        popUpTo = Screen.Splash.route,
                    )
                },
                onNavigateToHome = {
                    navController.navigateAndClearBackStack(
                        route = Screen.MainShell.route,
                        popUpTo = Screen.Splash.route,
                    )
                },
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToSignIn = {
                    navController.navigateAndClearBackStack(
                        route = Screen.SignIn.route,
                        popUpTo = Screen.Onboarding.route,
                    )
                },
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigateAndClearBackStack(
                        route = Screen.MainShell.route,
                        popUpTo = Screen.SignIn.route,
                    )
                },
                onNavigateToSignUp = { navController.navigateSingleTop(Screen.SignUp.route) },
                onForgotPassword = { /* TODO */ },
                onGoogleSignIn = { /* TODO */ },
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigateAndClearBackStack(
                        route = Screen.MainShell.route,
                        popUpTo = Screen.SignUp.route,
                    )
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigateAndClearBackStack(
                        route = Screen.SignIn.route,
                        popUpTo = Screen.SignUp.route,
                    )
                },
            )
        }

        composable(Screen.MainShell.route) {
            MainShell()
        }
    }
}