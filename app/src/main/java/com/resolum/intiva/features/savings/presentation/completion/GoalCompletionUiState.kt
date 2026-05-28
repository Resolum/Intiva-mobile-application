package com.resolum.intiva.features.savings.presentation.completion

import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * UI state for goal completion flows (US-022).
 *
 * Sealed hierarchy mirrors async outcomes: loading, completed, uncompleted, or error.
 */
sealed class GoalCompletionUiState {
  /** Goal data is being loaded from the API. */
  data object Loading : GoalCompletionUiState()

  /** Goal was marked COMPLETED; show celebration screen. */
  data class Completed(val goal: SavingGoal) : GoalCompletionUiState()

  /** Goal was marked UNCOMPLETED; show neutral outcome screen. */
  data class Uncompleted(val goal: SavingGoal) : GoalCompletionUiState()

  /** An error occurred while loading or updating the goal. */
  data class Error(val message: String) : GoalCompletionUiState()
}
