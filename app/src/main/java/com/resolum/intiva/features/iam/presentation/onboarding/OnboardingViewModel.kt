package com.resolum.intiva.features.iam.presentation.onboarding

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.models.FirstTransactionTutorialStep
import com.resolum.intiva.features.iam.domain.usecase.AdvanceOnboardingStepUseCase
import com.resolum.intiva.features.iam.domain.usecase.CompleteOnboardingUseCase
import com.resolum.intiva.features.iam.domain.usecase.GetOnboardingStatusUseCase
import com.resolum.intiva.features.iam.domain.usecase.GetStartDestinationUseCase
import com.resolum.intiva.features.iam.domain.usecase.RollbackOnboardingStepUseCase
import com.resolum.intiva.features.iam.presentation.splash.SplashDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for the Onboarding flow.
 *
 * Responsibilities:
 * - Determine the initial navigation destination (onboarding vs main app) based on user state.
 * - Mark onboarding as completed when the user finishes the flow.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getStartDestination: GetStartDestinationUseCase,
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val advanceOnboardingStepUseCase: AdvanceOnboardingStepUseCase,
    private val rollbackOnboardingStepUseCase: RollbackOnboardingStepUseCase
) : BaseViewModel() {

    /**
     * Exposes the navigation destination for the SplashScreen to observe.
     * Initially null until the destination is determined.
     */
    private val _destination = MutableStateFlow<SplashDestination?>(null)

    /** Backing property for the onboarding UI state, initialized with a default OnboardingUiState. */
    private val _state = MutableStateFlow(OnboardingUiState())
    val state = _state.asStateFlow()

    /** Public read-only access to the navigation destination */
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    /** Determines the initial navigation destination when the ViewModel is created. */
    init {
        safeLaunch {
            _destination.value = getStartDestination()
        }
    }

    /** Called when the user finishes the onboarding flow. Marks onboarding as completed. */
    fun onOnboardingFinished() = safeLaunch {
        completeOnboarding()
    }

    /** Loads the current onboarding status and updates the UI state accordingly. */
    fun loadStatus() = safeLaunch {
        _state.value = _state.value.copy(loading = true)

        when (val result = getOnboardingStatusUseCase()) {

            is NetworkResult.Success -> {
                _state.value = OnboardingUiState(
                    step = FirstTransactionTutorialStep.valueOf(
                        result.data.currentStep
                    )
                )
            }

            is NetworkResult.Error -> {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    /** Advances the onboarding step to the next one. */
    fun advance() = safeLaunch {
        advanceOnboardingStepUseCase()
        loadStatus()
    }

    /** Rolls back the onboarding step to the previous one. */
    fun rollback() = safeLaunch {
        rollbackOnboardingStepUseCase()
        loadStatus()
    }
}