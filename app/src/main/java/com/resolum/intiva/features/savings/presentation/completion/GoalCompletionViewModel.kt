package com.resolum.intiva.features.savings.presentation.completion

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for [GoalCompletedScreen] and [GoalUncompletedScreen].
 *
 * Loads the saving goal by ID and exposes [GoalCompletionUiState] for the UI.
 * Follows the [com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.CategoryViewModel] pattern.
 */
@HiltViewModel
class GoalCompletionViewModel @Inject constructor(
  private val getSavingGoalUseCase: GetSavingGoalUseCase
) : BaseViewModel() {

  private val _uiState = MutableStateFlow<GoalCompletionUiState>(GoalCompletionUiState.Loading)
  val uiState: StateFlow<GoalCompletionUiState> = _uiState.asStateFlow()

  /**
   * Loads the goal and maps its [com.resolum.intiva.features.savings.domain.models.SavingGoal.status]
   * to [GoalCompletionUiState.Completed] or [GoalCompletionUiState.Uncompleted].
   */
  fun loadGoal(accountId: Long, goalId: Long) {
    safeLaunch {
      _uiState.value = GoalCompletionUiState.Loading
      when (val result = getSavingGoalUseCase(accountId, goalId)) {
        is NetworkResult.Success -> {
          val goal = result.data
          _uiState.value = when (SavingGoalStatus.from(goal.status)) {
            SavingGoalStatus.COMPLETED -> GoalCompletionUiState.Completed(goal)
            SavingGoalStatus.UNCOMPLETED -> GoalCompletionUiState.Uncompleted(goal)
            SavingGoalStatus.INPROGRESS -> GoalCompletionUiState.Completed(goal)
          }
        }
        is NetworkResult.Error -> _uiState.value = GoalCompletionUiState.Error(
          result.message ?: "No se pudo cargar la meta."
        )
      }
    }
  }

  override fun handleError(throwable: Throwable) {
    _uiState.value = GoalCompletionUiState.Error(
      throwable.message ?: "Ocurrió un error inesperado."
    )
  }
}
