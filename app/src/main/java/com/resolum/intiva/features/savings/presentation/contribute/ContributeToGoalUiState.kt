package com.resolum.intiva.features.savings.presentation.contribute

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * UI state for the [ContributeToGoalScreen].
 *
 * Mirrors the [CategoryUiState] pattern: a data class holding a stable [SavingGoal]
 * reference alongside a generic [UiState] that tracks the async operation status.
 *
 * @property goal           The saving goal currently loaded (null until first fetch succeeds).
 * @property goalState      Tracks the loading/success/error state of the goal fetch + contribution call.
 * @property amountInput    The raw string the user has typed into the numeric keypad.
 * @property inputError     Inline validation error message for the amount field; null when valid.
 */
data class ContributeToGoalUiState(
    val goal: SavingGoal? = null,
    val goalState: UiState<SavingGoal> = UiState.Idle,
    val amountInput: String = "",
    val inputError: String? = null,
    /** Logged-in user ID from [SessionRepository]; required for the contribution POST body. */
    val contributorId: Long? = null,
    val isContributorIdLoading: Boolean = true
)
