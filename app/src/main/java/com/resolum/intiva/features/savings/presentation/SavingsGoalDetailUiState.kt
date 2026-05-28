package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * UI state for [SavingsGoalDetailScreen].
 *
 * Mirrors [com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.CategoryUiState]:
 * stable data fields plus a generic [UiState] for async loading.
 */
data class SavingsGoalDetailUiState(
  val goal: SavingGoal? = null,
  val contributions: List<GoalContribution> = emptyList(),
  val goalState: UiState<SavingGoal> = UiState.Idle
)
