package com.resolum.intiva.features.onboarding.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.onboarding.presentation.components.BottomOnboardingIndicators
import com.resolum.intiva.features.onboarding.presentation.components.OnboardingButton
import com.resolum.intiva.features.onboarding.presentation.components.OnboardingSlide1Content
import com.resolum.intiva.features.onboarding.presentation.components.OnboardingSlide2Content
import com.resolum.intiva.features.onboarding.presentation.components.OnboardingSlide3Content
import com.resolum.intiva.features.onboarding.presentation.components.SplashScreenContent
import com.resolum.intiva.features.onboarding.presentation.components.TopOnboardingIndicators
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * OnboardingScreen orchestrates the premium launch flow of the Intiva application.
 * It coordinates the animated Splash screen transition, the HorizontalPager,
 * page indicator layout configurations, and background theme color morphing.
 */
@Composable
fun OnboardingScreen(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSplash by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(2200)
        showSplash = false
    }

    val targetBgColor = when {
        showSplash -> IntivaColors.BackgroundPurple
        pagerState.currentPage == 1 -> IntivaColors.BackgroundPurple
        else -> IntivaColors.BackgroundLavender
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 600),
        label = "bgColorTransition"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            SplashScreenContent()
        }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Diapositiva 2 displays indicators at the top
                if (pagerState.currentPage == 1) {
                    Spacer(modifier = Modifier.height(54.dp))
                    TopOnboardingIndicators(currentPage = pagerState.currentPage)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    when (page) {
                        0 -> OnboardingSlide1Content()
                        1 -> OnboardingSlide2Content()
                        2 -> OnboardingSlide3Content()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (pagerState.currentPage != 1) {
                        BottomOnboardingIndicators(currentPage = pagerState.currentPage)
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    val buttonText = if (pagerState.currentPage == 2) "Empezar" else "Continuar"
                    OnboardingButton(
                        text = buttonText,
                        onClick = {
                            if (pagerState.currentPage < 2) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                onNavigateToSignIn()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
