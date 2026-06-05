package com.resolum.intiva.features.iam.presentation.onboarding

import com.resolum.intiva.features.iam.domain.models.FirstTransactionTutorialStep

/**
 * Data class representing the UI state for the onboarding process.
 *
 * @property step The current step in the first transaction tutorial, if applicable.
 * @property loading A boolean indicating whether the onboarding data is currently being loaded.
 */
data class OnboardingUiState(
    val step: FirstTransactionTutorialStep? = null,
    val loading: Boolean = false,
)
