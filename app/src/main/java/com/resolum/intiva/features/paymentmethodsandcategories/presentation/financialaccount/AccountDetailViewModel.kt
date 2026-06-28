package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.lifecycle.SavedStateHandle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.usecase.GetTransactionsByOwnerIdUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.DisableFinancialAccountUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetFinancesAccountUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.UpdateFinancialAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFinancesAccountUseCase: GetFinancesAccountUseCase,
    private val disableFinancialAccountUseCase: DisableFinancialAccountUseCase,
    private val updateFinancialAccountUseCase: UpdateFinancialAccountUseCase,
    private val getTransactionsByOwnerIdUseCase: GetTransactionsByOwnerIdUseCase
) : BaseViewModel() {

    private val accountId: Long =
        savedStateHandle.get<String>("accountId")?.toLongOrNull() ?: -1L

    private val _uiState = MutableStateFlow(AccountDetailUiState())
    val uiState: StateFlow<AccountDetailUiState> = _uiState.asStateFlow()

    fun loadAccount() {
        safeLaunch {
            _uiState.update { it.copy(accountState = UiState.Loading) }
            val account = getFinancesAccountUseCase.getById(accountId)
            if (account != null) {
                _uiState.update {
                    it.copy(account = account, accountState = UiState.Success(account))
                }
                loadRecentTransactions()
            } else {
                _uiState.update {
                    it.copy(accountState = UiState.Error("Cuenta no encontrada"))
                }
            }
        }
    }

    private fun loadRecentTransactions() {
        safeLaunch {
            _uiState.update { it.copy(transactionsState = UiState.Loading) }
            when (val result = getTransactionsByOwnerIdUseCase()) {
                is NetworkResult.Success -> {
                    val filtered = result.data
                        .flatMap { it.transactions }
                        .filter { it.financialAccountId == accountId }
                        .take(5)
                    _uiState.update {
                        it.copy(
                            recentTransactions = filtered,
                            transactionsState = UiState.Success(filtered)
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(transactionsState = UiState.Error(result.message, result.throwable))
                    }
                }
            }
        }
    }

    fun disableAccount() {
        safeLaunch {
            _uiState.update { it.copy(accountState = UiState.Loading) }
            when (val result = disableFinancialAccountUseCase(accountId)) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(account = result.data, accountState = UiState.Success(result.data))
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(accountState = UiState.Error(result.message, result.throwable))
                }
            }
        }
    }

    fun updateName(newName: String) {
        safeLaunch {
            when (val result =
                updateFinancialAccountUseCase(accountId = accountId, name = newName)) {
                is NetworkResult.Success -> _uiState.update { it.copy(account = result.data) }
                is NetworkResult.Error -> { /* handled by BaseViewModel */ }
            }
        }
    }
}
