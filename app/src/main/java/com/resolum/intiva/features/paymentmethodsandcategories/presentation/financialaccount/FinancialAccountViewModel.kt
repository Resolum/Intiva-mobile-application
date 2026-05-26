package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetFinancesAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for managing the state of the financial account screen, including fetching financial accounts
 * and handling loading and error states.
 *
 * @property getFinancialAccountsUseCase Use case for fetching financial accounts from the repository.
 */
@HiltViewModel
class FinancialAccountViewModel @Inject constructor(
    private val getFinancialAccountsUseCase: GetFinancesAccountUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(FinancialAccountUiState())
    val uiState: StateFlow<FinancialAccountUiState> = _uiState.asStateFlow()

    fun getFinancialAccounts() {
        safeLaunch {
            _uiState.update { it.copy(accountsState = UiState.Loading) }
            when (val result = getFinancialAccountsUseCase()) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        accounts = result.data,
                        accountsState = UiState.Success(result.data)
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        accountsState = UiState.Error(
                            message = result.message,
                            throwable = result.throwable
                        )
                    )
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                accountsState = UiState.Error(
                    message = throwable.message ?: "An error occurred while fetching financial accounts",
                    throwable = throwable
                )
            )
        }
    }
}