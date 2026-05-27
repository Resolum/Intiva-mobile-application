package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoalOwnerType
import com.resolum.intiva.features.savings.domain.usecases.CreateSavingGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetSavingsAccountIdUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * ViewModel for [SavingsGoalCreateScreen].
 *
 * Holds form state, validates user input, and submits the create-goal POST.
 */
@HiltViewModel
class SavingsGoalCreateViewModel @Inject constructor(
    private val getSavingsAccountIdUseCase: GetSavingsAccountIdUseCase,
    private val createSavingGoalUseCase: CreateSavingGoalUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SavingsGoalCreateUiState())
    val uiState: StateFlow<SavingsGoalCreateUiState> = _uiState.asStateFlow()

    /**
     * Called from the screen with the navigation [accountId]; falls back to session resolution.
     */
    fun initialize(accountId: Long) {
        _uiState.update { it.copy(accountId = accountId) }
        loadFamilyGroups()
    }

    fun updateTitle(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun updateTargetAmount(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(targetAmountInput = filtered, targetAmountError = null) }
    }

    fun setPersonalGoal() {
        _uiState.update {
            it.copy(isFamilyGoal = false, groupError = null)
        }
    }

    fun setFamilyGoal() {
        _uiState.update { it.copy(isFamilyGoal = true) }
        if (_uiState.value.availableGroups.isEmpty()) {
            loadFamilyGroups()
        }
    }

    fun selectGroup(groupId: Long) {
        _uiState.update { it.copy(selectedGroupId = groupId, groupError = null) }
    }

    fun updateDeadline(date: LocalDate) {
        _uiState.update { it.copy(deadline = date, deadlineError = null) }
    }

    /**
     * Validates all fields and, if valid, calls the create API.
     */
    fun submitGoal() {
        val state = _uiState.value
        if (!validateForm(markErrors = true)) return

        val targetAmount = state.targetAmountInput.toBigDecimalOrNull() ?: return
        val deadlineIso = state.deadline?.atStartOfDay()?.format(ISO_DEADLINE_FORMATTER) ?: return
        val ownerType = if (state.isFamilyGoal) SavingGoalOwnerType.FAMILY else SavingGoalOwnerType.INDIVIDUAL

        safeLaunch {
            _uiState.update { it.copy(isLoading = true, submitError = null) }

            val accountId = state.accountId ?: resolveAccountId() ?: run {
                _uiState.update {
                    it.copy(isLoading = false, submitError = "No se pudo obtener la cuenta.")
                }
                return@safeLaunch
            }

            when (
                val result = createSavingGoalUseCase(
                    accountId = accountId,
                    title = state.title.trim(),
                    targetAmount = targetAmount,
                    currencyCode = state.currencyCode,
                    deadline = deadlineIso,
                    ownerType = ownerType,
                    categoryId = state.categoryId
                )
            ) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(isLoading = false, createdGoal = result.data)
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        submitError = result.message ?: "No se pudo crear la meta."
                    )
                }
            }
        }
    }

    /** Clears [SavingsGoalCreateUiState.createdGoal] after navigation consumes it. */
    fun onGoalCreationHandled() {
        _uiState.update { it.copy(createdGoal = null) }
    }

    /**
     * Returns the target amount formatted for the large amount preview card.
     */
    fun formattedTargetAmountPreview(): String {
        val amount = _uiState.value.targetAmountInput.toBigDecimalOrNull() ?: return "0"
        return previewGoal(amount).formatAmount(amount)
    }

    private fun validateForm(markErrors: Boolean): Boolean {
        val state = _uiState.value
        var valid = true

        val titleError = when {
            state.title.isBlank() -> "El nombre de la meta es obligatorio."
            else -> null
        }
        if (titleError != null) valid = false

        val amount = state.targetAmountInput.toBigDecimalOrNull()
        val amountError = when {
            amount == null || amount <= BigDecimal.ZERO -> "El monto debe ser mayor a 0."
            else -> null
        }
        if (amountError != null) valid = false

        val deadlineError = when {
            state.deadline == null -> "Selecciona una fecha límite."
            !state.deadline.isAfter(LocalDate.now()) -> "La fecha límite debe ser futura."
            else -> null
        }
        if (deadlineError != null) valid = false

        val groupError = if (state.isFamilyGoal && state.selectedGroupId == null) {
            "Selecciona un grupo familiar."
        } else null
        if (groupError != null) valid = false

        if (markErrors) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    targetAmountError = amountError,
                    deadlineError = deadlineError,
                    groupError = groupError
                )
            }
        }
        return valid
    }

    private suspend fun resolveAccountId(): Long? {
        return try {
            val result = getSavingsAccountIdUseCase()
            _uiState.update { it.copy(accountId = result) }
            result
        } catch (_: Exception) {
            null
        }
    }

    private fun loadFamilyGroups() {
        safeLaunch {
            when (val result = getCategoriesUseCase()) {
                is NetworkResult.Success -> {
                    val groups = result.data
                        .mapNotNull { category ->
                            category.groupId?.let { id ->
                                FamilyGroupOption(groupId = id, label = category.name)
                            }
                        }
                        .distinctBy { it.groupId }
                    _uiState.update { state ->
                        state.copy(
                            availableGroups = groups,
                            selectedGroupId = state.selectedGroupId ?: groups.firstOrNull()?.groupId
                        )
                    }
                }
                is NetworkResult.Error -> Unit
            }
        }
    }

  private fun previewGoal(amount: BigDecimal) =
        com.resolum.intiva.features.savings.domain.models.SavingGoal(
            id = 0,
            ownerType = SavingGoalOwnerType.INDIVIDUAL,
            actorUserId = 0,
            ownerId = "",
            title = "",
            currentAmount = amount,
            targetAmount = amount,
            currencyCode = _uiState.value.currencyCode,
            deadline = "",
            status = "INPROGRESS",
            categoryId = 0
        )

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(isLoading = false, submitError = throwable.message ?: "Error inesperado.")
        }
    }

    companion object {
        private val ISO_DEADLINE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }
}
