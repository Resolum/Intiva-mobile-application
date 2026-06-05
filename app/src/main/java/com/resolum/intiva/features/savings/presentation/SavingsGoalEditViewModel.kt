package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import com.resolum.intiva.features.savings.domain.usecases.CompleteGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.UncompleteGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.UpdateSavingGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import javax.inject.Inject

data class SavingsGoalEditUiState(
    val savingGoalId: Long = 0L,
    val title: String = "",
    val description: String = "",
    val targetAmountInput: String = "",
    val currentStatus: SavingGoalStatus = SavingGoalStatus.INPROGRESS,
    val deadline: String = "",
    val titleError: String? = null,
    val targetAmountError: String? = null,
    val isLoading: Boolean = false,
    val isStatusLoading: Boolean = false,
    val isSaved: Boolean = false,
    val goalJustCompleted: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class SavingsGoalEditViewModel @Inject constructor(
    private val getSavingGoalUseCase: GetSavingGoalUseCase,
    private val updateSavingGoalUseCase: UpdateSavingGoalUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val uncompleteGoalUseCase: UncompleteGoalUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SavingsGoalEditUiState())
    val uiState: StateFlow<SavingsGoalEditUiState> = _uiState.asStateFlow()

    fun loadGoal(savingGoalId: Long) {
        safeLaunch {
            _uiState.update { it.copy(isLoading = true, savingGoalId = savingGoalId) }
            when (val result = getSavingGoalUseCase(savingGoalId)) {
                is NetworkResult.Success -> {
                    val goal = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = goal.title,
                            description = goal.description,
                            targetAmountInput = goal.targetAmount.toPlainString(),
                            currentStatus = SavingGoalStatus.from(goal.status),
                            deadline = goal.deadline
                        )
                    }
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun updateTitle(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun updateDescription(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun updateTargetAmount(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(targetAmountInput = filtered, targetAmountError = null) }
    }

    fun save() {
        val state = _uiState.value

        val titleError = if (state.title.isBlank()) "El nombre es obligatorio." else null
        val amount = state.targetAmountInput.toBigDecimalOrNull()
        val amountError = if (amount == null || amount <= BigDecimal.ZERO) "El monto debe ser mayor a 0." else null

        _uiState.update { it.copy(titleError = titleError, targetAmountError = amountError) }
        if (titleError != null || amountError != null) return

        safeLaunch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = updateSavingGoalUseCase(
                savingGoalId = state.savingGoalId,
                title = state.title.trim(),
                description = state.description.trim().ifBlank { null },
                newTargetAmount = amount
            )) {
                is NetworkResult.Success -> _uiState.update { it.copy(isLoading = false, isSaved = true) }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message ?: "No se pudo guardar los cambios.")
                }
            }
        }
    }

    fun markAsCompleted() {
        val goalId = _uiState.value.savingGoalId
        safeLaunch {
            _uiState.update { it.copy(isStatusLoading = true, error = null) }
            when (val result = completeGoalUseCase(goalId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        isStatusLoading = false,
                        currentStatus = SavingGoalStatus.COMPLETED,
                        goalJustCompleted = true
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(isStatusLoading = false, error = result.message ?: "No se pudo marcar como completada.")
                }
            }
        }
    }
    fun markAsUncompleted() {
        val goalId = _uiState.value.savingGoalId
        safeLaunch {
            _uiState.update { it.copy(isStatusLoading = true, error = null) }
            when (val result = uncompleteGoalUseCase(goalId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        isStatusLoading = false,
                        currentStatus = SavingGoalStatus.UNCOMPLETED,
                        successMessage = "Meta revertida a no completada."
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(isStatusLoading = false, error = result.message ?: "No se pudo revertir el estado.")
                }
            }
        }
    }

    fun onSaveHandled() {
        _uiState.update { it.copy(isSaved = false) }
    }

    fun onGoalCompletedHandled() {
        _uiState.update { it.copy(goalJustCompleted = false) }
    }

    fun onMessageHandled() {
        _uiState.update { it.copy(successMessage = null, error = null) }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update { it.copy(isLoading = false, error = throwable.message ?: "Error inesperado.") }
    }
}
