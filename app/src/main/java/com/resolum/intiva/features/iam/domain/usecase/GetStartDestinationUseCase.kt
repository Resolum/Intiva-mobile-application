package com.resolum.intiva.features.iam.domain.usecase

import com.resolum.intiva.features.iam.domain.repositories.OnboardingRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.iam.presentation.splash.SplashDestination
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case to determine the initial navigation destination when the app starts.
 *
 * This use case checks whether the user has completed the onboarding process and whether they are authenticated
 * to decide which screen to navigate to: Onboarding, SignIn, or Home.
 */
class GetStartDestinationUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val onboardingRepository: OnboardingRepository,
) {
    /** Determines the initial navigation destination based on onboarding and authentication status.
     *
     * This function checks if the user has seen the onboarding screen and if they have a valid authentication token.
     * It returns a [SplashDestination] indicating where the app should navigate to on startup.
     *
     * @return [SplashDestination] The destination to navigate to (Onboarding, SignIn, or Home).
     */
    suspend operator fun invoke(): SplashDestination {
        val hasSeenOnboarding = onboardingRepository.hasSeenOnboarding.first()
        val token             = sessionRepository.authToken.first()

        return when {
            !hasSeenOnboarding    -> SplashDestination.Onboarding
            token.isNullOrBlank() -> SplashDestination.SignIn
            else                  -> SplashDestination.Home
        }
    }
}