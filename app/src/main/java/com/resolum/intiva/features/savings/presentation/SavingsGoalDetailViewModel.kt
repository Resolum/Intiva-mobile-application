package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SavingsGoalDetailViewModel @Inject constructor(
    private val getSavingGoalUseCase: GetSavingGoalUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SavingsGoalDetailUiState())
    val uiState: StateFlow<SavingsGoalDetailUiState> = _uiState.asStateFlow()

    fun loadGoal(accountId: Long, goalId: Long) {
        safeLaunch {
            _uiState.update { it.copy(goalState = UiState.Loading) }
            when (val result = getSavingGoalUseCase(goalId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(goal = result.data, goalState = UiState.Success(result.data))
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(goalState = UiState.Error(message = result.message, throwable = result.throwable))
                }
            }
        }
    }

    fun refreshGoal(accountId: Long, goalId: Long) = loadGoal(accountId, goalId)

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(goalState = UiState.Error(message = throwable.message ?: "Error al cargar la meta.", throwable = throwable))
        }
    }
}
