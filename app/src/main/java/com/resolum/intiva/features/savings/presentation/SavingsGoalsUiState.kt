package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * UI state for [SavingsGoalsScreen] goal list loading.
 */
sealed class SavingsGoalsUiState {
    data object Loading : SavingsGoalsUiState()
    data class Success(val goals: List<SavingGoal>) : SavingsGoalsUiState()
    data class Error(val message: String) : SavingsGoalsUiState()
}

/**
 * Holds screen-level data alongside the goals list state.
 */
data class SavingsGoalsScreenState(
    val accountId: Long? = null,
    val groupId: Long? = null,
    val selectedTab: Int = 0,
    val goalsState: SavingsGoalsUiState = SavingsGoalsUiState.Loading
)
