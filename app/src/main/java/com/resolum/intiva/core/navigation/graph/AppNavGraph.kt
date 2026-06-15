package com.resolum.intiva.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.resolum.intiva.core.navigation.extensions.navigateAndClearBackStack
import com.resolum.intiva.core.navigation.extensions.navigateSingleTop
import com.resolum.intiva.core.navigation.routes.NavRoutes
import com.resolum.intiva.core.navigation.routes.Screen
import com.resolum.intiva.features.household.presentation.invitation.InvitationDetailScreen
import com.resolum.intiva.features.iam.presentation.PrivacyPolicyScreen
import com.resolum.intiva.features.iam.presentation.TermsAndConditionsScreen
import com.resolum.intiva.features.profiles.presentation.onboarding.OnboardingScreen
import com.resolum.intiva.features.iam.presentation.signin.SignInScreen
import com.resolum.intiva.features.iam.presentation.signup.SignUpScreen
import com.resolum.intiva.features.iam.presentation.splash.SplashScreen

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
                        route = Screen.SignIn.route,
                        popUpTo = Screen.SignUp.route,
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToTermsAndConditions = {
                    navController.navigate(Screen.TermsAndConditions.route)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Screen.PrivacyPolicy.route)
                },
                onNavigateToLogin = {
                    navController.navigateAndClearBackStack(
                        route = Screen.SignIn.route,
                        popUpTo = Screen.SignUp.route,
                    )
                },
            )
        }


        composable(Screen.TermsAndConditions.route) {
            TermsAndConditionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.MainShell.route) {
            MainShell(
                onLogout = {
                    navController.navigateAndClearBackStack(
                        route = Screen.SignIn.route,
                        popUpTo = Screen.MainShell.route,
                    )
                }
            )
        }

        composable(
            route = NavRoutes.INVITATION_DEEP_LINK,
            arguments = listOf(navArgument("token") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://intiva.vercel.app/lander?token={token}" },
                navDeepLink { uriPattern = "https://intiva.vercel.app/join?token={token}" },
                navDeepLink { uriPattern = "https://intiva-1406c.web.app/lander?token={token}" },
                navDeepLink { uriPattern = "intiva://lander?token={token}" },
                navDeepLink { uriPattern = "intiva://join?token={token}" }
            )
        ) {
            InvitationDetailScreen(
                onAccepted = {
                    navController.navigateAndClearBackStack(
                        route = Screen.MainShell.route,
                        popUpTo = NavRoutes.INVITATION_DEEP_LINK,
                    )
                },
                onDeclined = {
                    navController.navigateAndClearBackStack(
                        route = Screen.MainShell.route,
                        popUpTo = NavRoutes.INVITATION_DEEP_LINK,
                    )
                },
                onGoToLanding = {
                    navController.popBackStack()
                }
            )
        }
    }
}