package com.resolum.intiva.core.navigation.routes


/**
 * Sealed class containing navigation routes for unauthenticated screens.
 * These are screens accessible before the user logs in.
 */
sealed class Screen(val route: String) {
    data object Splash              : Screen("splash")
    data object Onboarding          : Screen("onboarding")
    data object SignIn              : Screen("sign_in")
    data object SignUp              : Screen("sign_up")

    data object TermsAndConditions  : Screen("terms_and_conditions")
    data object PrivacyPolicy       : Screen("privacy_policy")
    data object MainShell           : Screen("main_shell")
}