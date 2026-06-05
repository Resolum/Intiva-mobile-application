package com.resolum.intiva.features.savings.presentation.completion

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.UncompleteGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GoalCompletionViewModel @Inject constructor(
    private val getSavingGoalUseCase: GetSavingGoalUseCase,
    private val uncompleteGoalUseCase: UncompleteGoalUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<GoalCompletionUiState>(GoalCompletionUiState.Loading)
    val uiState: StateFlow<GoalCompletionUiState> = _uiState.asStateFlow()

    fun loadGoal(accountId: Long, goalId: Long) {
        safeLaunch {
            _uiState.value = GoalCompletionUiState.Loading
            when (val result = getSavingGoalUseCase(goalId)) {
                is NetworkResult.Success -> {
                    val goal = result.data
                    _uiState.value = when (SavingGoalStatus.from(goal.status)) {
                        SavingGoalStatus.COMPLETED -> GoalCompletionUiState.Completed(
                            goal = goal,
                            notification = GoalCompletionNotificationHelper.buildCompletedNotification(goal)
                        )
                        SavingGoalStatus.UNCOMPLETED -> GoalCompletionUiState.Uncompleted(
                            goal = goal,
                            notification = GoalCompletionNotificationHelper.buildUncompletedNotification(goal)
                        )
                        SavingGoalStatus.INPROGRESS -> GoalCompletionUiState.Completed(
                            goal = goal,
                            notification = GoalCompletionNotificationHelper.buildCompletedNotification(goal)
                        )
                    }
                }
                is NetworkResult.Error -> _uiState.value =
                    GoalCompletionUiState.Error(result.message ?: "No se pudo cargar la meta.")
            }
        }
    }

    fun uncompleteGoal(accountId: Long, goalId: Long) {
        safeLaunch { uncompleteGoalUseCase(goalId) }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.value = GoalCompletionUiState.Error(throwable.message ?: "Ocurrió un error inesperado.")
    }
}
