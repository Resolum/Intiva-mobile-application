package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.usecases.DeleteSavingGoalUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetFamilyGroupIdUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetGroupGoalsUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetSavingGoalsUseCase
import com.resolum.intiva.features.savings.domain.usecases.GetSavingsAccountIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for [SavingsGoalsScreen].
 *
 * Resolves [userId] (the authenticated user ID) exactly once on init
 * and loads personal or family goals per selected tab.
 */
@HiltViewModel
class SavingsGoalsViewModel @Inject constructor(
    private val getSavingsAccountIdUseCase: GetSavingsAccountIdUseCase,
    private val getFamilyGroupIdUseCase: GetFamilyGroupIdUseCase,
    private val getSavingGoalsUseCase: GetSavingGoalsUseCase,
    private val getGroupGoalsUseCase: GetGroupGoalsUseCase,
    private val deleteGoalUseCase: DeleteSavingGoalUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SavingsGoalsScreenState())
    val uiState: StateFlow<SavingsGoalsScreenState> = _uiState.asStateFlow()

    private var initialized = false

    init {
        // Run resolution and initial load exactly once to prevent re-triggering on recompositions
        if (!initialized) {
            initialized = true
            loadAccountIdAndGoals()
        }
    }

    /**
     * Resolves the savings account ID once on initialization, caches it,
     * and loads personal savings goals for the first tab.
     */
    private fun loadAccountIdAndGoals() {
        safeLaunch {
            _uiState.update { it.copy(goalsState = SavingsGoalsUiState.Loading) }
            try {
                val accountId = getSavingsAccountIdUseCase()
                _uiState.update { it.copy(accountId = accountId) }
                loadPersonalGoals(accountId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        goalsState = SavingsGoalsUiState.Error(
                            e.message ?: "No se pudo obtener el identificador de cuenta."
                        )
                    )
                }
            }
        }
    }

    /**
     * Resolves [accountId] from session so navigation (e.g. create goal) works even when
     * the goals list request fails.
     */
    fun navigateToCreate(onNavigate: (Long) -> Unit) {
        safeLaunch {
            val accountId = _uiState.value.accountId ?: resolveAccountIdSync()
            if (accountId != null) {
                onNavigate(accountId)
            } else {
                _uiState.update {
                    it.copy(
                        goalsState = SavingsGoalsUiState.Error(
                            "No se pudo obtener la cuenta. Verifica tu conexión e intenta de nuevo."
                        )
                    )
                }
            }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        if (_uiState.value.selectedTab == tabIndex) return
        _uiState.update { it.copy(selectedTab = tabIndex) }
        loadGoalsForTab(tabIndex)
    }

    fun refresh() {
        loadGoalsForTab(_uiState.value.selectedTab)
    }

    /**
     * Deletes the goal with the given ID and refreshes the list on success.
     */
    fun deleteGoal(goalId: Long) {
        safeLaunch {
            when (deleteGoalUseCase(goalId)) {
                is NetworkResult.Success -> refresh()
                is NetworkResult.Error -> Unit
            }
        }
    }

    /** Resolves and persists [SavingsGoalsScreenState.accountId]; returns null on failure. */
    private suspend fun resolveAccountIdSync(): Long? {
        val currentAccountId = _uiState.value.accountId
        if (currentAccountId != null) return currentAccountId

        return try {
            val resolved = getSavingsAccountIdUseCase()
            _uiState.update { it.copy(accountId = resolved) }
            resolved
        } catch (_: Exception) {
            null
        }
    }

    private fun loadGoalsForTab(tabIndex: Int) {
        safeLaunch {
            _uiState.update { it.copy(goalsState = SavingsGoalsUiState.Loading) }

            val accountId = resolveAccountIdSync()
            if (accountId == null) {
                _uiState.update {
                    it.copy(
                        goalsState = SavingsGoalsUiState.Error(
                            "No se pudo obtener la cuenta. Verifica tu conexión e intenta de nuevo."
                        )
                    )
                }
                return@safeLaunch
            }

            when (tabIndex) {
                0 -> loadPersonalGoals(accountId)
                else -> loadFamilyGoals(accountId)
            }
        }
    }

    private suspend fun loadPersonalGoals(accountId: Long) {
        when (val result = getSavingGoalsUseCase(accountId)) {
            is NetworkResult.Success -> _uiState.update {
                it.copy(goalsState = SavingsGoalsUiState.Success(result.data))
            }
            is NetworkResult.Error -> _uiState.update {
                it.copy(goalsState = SavingsGoalsUiState.Error(result.message ?: "Failed to load goals."))
            }
        }
    }

    private suspend fun loadFamilyGoals(accountId: Long) {
        when (val groupResult = getFamilyGroupIdUseCase()) {
            is NetworkResult.Error -> _uiState.update {
                it.copy(goalsState = SavingsGoalsUiState.Error(groupResult.message ?: "No family group found."))
            }
            is NetworkResult.Success -> {
                val groupId = groupResult.data
                _uiState.update { it.copy(groupId = groupId) }
                when (val goalsResult = getGroupGoalsUseCase(groupId.toString())) {
                    is NetworkResult.Success -> _uiState.update {
                        it.copy(goalsState = SavingsGoalsUiState.Success(goalsResult.data))
                    }
                    is NetworkResult.Error -> _uiState.update {
                        it.copy(goalsState = SavingsGoalsUiState.Error(goalsResult.message ?: "Failed to load family goals."))
                    }
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(goalsState = SavingsGoalsUiState.Error(throwable.message ?: "Unexpected error."))
        }
    }
}
