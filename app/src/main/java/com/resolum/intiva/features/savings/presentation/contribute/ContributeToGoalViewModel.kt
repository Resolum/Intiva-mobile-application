package com.resolum.intiva.features.savings.presentation.contribute

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.GoalCompletionEvaluator
import com.resolum.intiva.features.savings.domain.usecases.RegisterContributionUseCase
import com.resolum.intiva.features.savings.presentation.completion.GoalCompletionNotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import javax.inject.Inject

/**
 * ViewModel for [ContributeToGoalScreen].
 */
@HiltViewModel
class ContributeToGoalViewModel @Inject constructor(
    private val registerContributionUseCase: RegisterContributionUseCase,
    private val getSavingGoalUseCase: GetSavingGoalUseCase,
    private val goalCompletionEvaluator: GoalCompletionEvaluator,
    private val notificationHelper: GoalCompletionNotificationHelper,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ContributeToGoalUiState())
    val uiState: StateFlow<ContributeToGoalUiState> = _uiState.asStateFlow()

    private var currentAccountId: Long = 0L
    private var currentGoalId: Long = 0L

    init {
        loadContributorId()
    }

    /** Loads the authenticated user ID from session to use as [contributorId] in the API. */
    private fun loadContributorId() {
        safeLaunch {
            _uiState.update { it.copy(isContributorIdLoading = true) }
            val userId = sessionRepository.getUserId()
            _uiState.update {
                it.copy(
                    contributorId = userId,
                    isContributorIdLoading = false,
                    inputError = if (userId == null) {
                        "No se pudo identificar al usuario. Inicia sesión nuevamente."
                    } else null
                )
            }
        }
    }

    fun appendDigit(digit: String) {
        val current = _uiState.value.amountInput
        if (current.length >= 7) return
        if (digit == "." && current.contains(".")) return
        _uiState.update { it.copy(amountInput = current + digit, inputError = null) }
    }

    fun deleteLastDigit() {
        val current = _uiState.value.amountInput
        if (current.isNotEmpty()) {
            _uiState.update { it.copy(amountInput = current.dropLast(1), inputError = null) }
        }
    }

    fun loadGoal(accountId: Long, goalId: Long) {
        currentAccountId = accountId
        currentGoalId = goalId
        safeLaunch {
            _uiState.update { it.copy(goalState = UiState.Loading) }
            when (val result = getSavingGoalUseCase(accountId, goalId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(goal = result.data, goalState = UiState.Success(result.data))
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(goalState = UiState.Error(message = result.message, throwable = result.throwable))
                }
            }
        }
    }

    fun contribute(
        currencyCode: String,
        onCompleted: () -> Unit,
        onUncompleted: () -> Unit,
        onSuccess: (com.resolum.intiva.features.savings.domain.models.GoalContribution) -> Unit = {}
    ) {
        val contributorId = _uiState.value.contributorId
        if (contributorId == null) {
            _uiState.update {
                it.copy(inputError = "No se pudo identificar al usuario. Inicia sesión nuevamente.")
            }
            return
        }

        val amountStr = _uiState.value.amountInput
        val amount = amountStr.toBigDecimalOrNull()

        if (amount == null || amount <= BigDecimal.ZERO) {
            _uiState.update { it.copy(inputError = "Ingresa un monto válido mayor a 0.") }
            return
        }

        safeLaunch {
            _uiState.update { it.copy(goalState = UiState.Loading) }

            when (
                val result = registerContributionUseCase(
                    userId = currentAccountId,
                    savingGoalId = currentGoalId,
                    amount = amount,
                    currencyCode = currencyCode,
                    contributorId = contributorId
                )
            ) {
                is NetworkResult.Success -> {
                    val updatedGoal = result.data.updatedGoal
                    _uiState.update {
                        it.copy(
                            goal = updatedGoal,
                            goalState = UiState.Success(updatedGoal),
                            amountInput = ""
                        )
                    }
                    onSuccess(result.data.contribution)

                    when (goalCompletionEvaluator.evaluate(currentAccountId, updatedGoal)) {
                        is GoalCompletionEvaluator.CompletionResult.Completed -> {
                            notificationHelper.notifyStatusChange(updatedGoal.title, SavingGoalStatus.COMPLETED)
                            onCompleted()
                        }
                        is GoalCompletionEvaluator.CompletionResult.Uncompleted -> {
                            notificationHelper.notifyStatusChange(updatedGoal.title, SavingGoalStatus.UNCOMPLETED)
                            onUncompleted()
                        }
                        is GoalCompletionEvaluator.CompletionResult.NoChange -> Unit
                    }
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        goalState = UiState.Error(message = result.message, throwable = result.throwable),
                        inputError = result.message
                    )
                }
            }
        }
    }

    fun currentGoal(): SavingGoal? = _uiState.value.goal

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                goalState = UiState.Error(
                    message = throwable.message ?: "An unexpected error occurred.",
                    throwable = throwable
                )
            )
        }
    }
}
