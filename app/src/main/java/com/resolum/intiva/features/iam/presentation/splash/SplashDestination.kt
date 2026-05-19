package com.resolum.intiva.features.iam.presentation.splash

/**
 * Sealed class representing the possible navigation destinations from the splash screen.
 */
sealed class SplashDestination {

    /**
     * Represents the onboarding screen, shown to users who haven't completed onboarding.
     */
    data object Onboarding : SplashDestination()

    /**
     * Represents the sign-in screen, shown to users who need to authenticate.
     */
    data object SignIn     : SplashDestination()

    /**
     * Represents the home screen, shown to users who are already authenticated and have completed onboarding.
     */
    data object Home       : SplashDestination()
}