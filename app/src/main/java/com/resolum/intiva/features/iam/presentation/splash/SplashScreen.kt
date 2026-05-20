package com.resolum.intiva.features.iam.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.iam.presentation.onboarding.OnboardingViewModel
import com.resolum.intiva.features.iam.presentation.splash.components.SplashScreenContent
import kotlinx.coroutines.delay

/**
 * Composable for the splash screen.
 *
 * Displays a loading animation while determining the next navigation destination
 * (onboarding, sign-in, or home) based on the user's state.
 *
 * @param viewModel The [OnboardingViewModel] to determine the navigation destination.
 * @param onNavigateToOnboarding Callback to navigate to the onboarding screen.
 * @param onNavigateToSignIn Callback to navigate to the sign-in screen.
 * @param onNavigateToHome Callback to navigate to the home screen.
 * @param modifier Optional [Modifier] for styling.
 */
@Composable
fun SplashScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToOnboarding: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
        )
    }

    LaunchedEffect(destination) {
        if (destination != null) {
            delay(1000)
            when (destination) {
                SplashDestination.Onboarding -> onNavigateToOnboarding()
                SplashDestination.SignIn     -> onNavigateToSignIn()
                SplashDestination.Home       -> onNavigateToHome()
                null                         -> Unit
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IntivaColors.BackgroundPurple)
    ) {
        SplashScreenContent(
            progress = progress.value
        )
    }
}
