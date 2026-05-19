package com.resolum.intiva.features.iam.presentation.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.iam.presentation.onboarding.components.BottomOnboardingIndicators
import com.resolum.intiva.features.iam.presentation.onboarding.components.OnboardingButton
import com.resolum.intiva.features.iam.presentation.onboarding.components.TopOnboardingIndicators
import com.resolum.intiva.features.iam.presentation.onboarding.components.familygoals.OnboardingFamilyScreen
import com.resolum.intiva.features.iam.presentation.onboarding.components.financegoals.OnboardingFinanceScreen
import com.resolum.intiva.features.iam.presentation.onboarding.components.savinggoals.OnboardingSavingScreen
import kotlinx.coroutines.launch

/**
 * OnboardingScreen - Main composable for the onboarding flow.
 *
 * Displays a horizontal pager with 3 slides, each showcasing a key feature of the app:
 * 1. Financial Goals
 * 2. Saving Goals
 * 3. Family Finance Management
 *
 * The background color transitions smoothly between slides, and indicators show the current page.
 * The "Continue" button advances through the slides, changing to "Get Started" on the last page.
 */
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val targetBgColor = when (pagerState.currentPage) {
        1    -> IntivaColors.BackgroundPurple
        else -> IntivaColors.BackgroundLavender
    }

    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 600),
        label = "bgColorTransition",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (pagerState.currentPage == 1) {
                Spacer(modifier = Modifier.height(54.dp))
                TopOnboardingIndicators(currentPage = pagerState.currentPage)
                Spacer(modifier = Modifier.height(16.dp))
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                when (page) {
                    0 -> OnboardingFinanceScreen()
                    1 -> OnboardingSavingScreen()
                    2 -> OnboardingFamilyScreen()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                            viewModel.onOnboardingFinished()
                            onNavigateToSignIn()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}