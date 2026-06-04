package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.CreateFinancialAccountUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.DisableFinancialAccountUseCase
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
    private val getFinancialAccountsUseCase: GetFinancesAccountUseCase,
    private val createFinancialAccountUseCase: CreateFinancialAccountUseCase,
    private val disableFinancialAccountUseCase: DisableFinancialAccountUseCase
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

    fun createFinancialAccount(
        name: String,
        accountType: String,
        currencyCode: String,
        currentAmount: Double,
        institution: String?,
        creditLimit: Double?
    ) {
        safeLaunch {
            _uiState.update { it.copy(createAccountState = UiState.Loading) }
            when (val result = createFinancialAccountUseCase(
                name = name,
                accountType = accountType,
                currencyCode = currencyCode,
                currentAmount = currentAmount,
                institution = institution,
                creditLimit = creditLimit
            )) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        createAccountState = UiState.Success(result.data),
                        accounts = it.accounts + result.data
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        createAccountState = UiState.Error(
                            message = result.message,
                            throwable = result.throwable
                        )
                    )
                }
            }
        }
    }

    fun onDisableAccountClick(account: FinancialAccount) {
        _uiState.update {
            it.copy(
                accountToDisable = account,
                showDisableConfirmDialog = true
            )
        }
    }

    fun onDismissDisableDialog() {
        _uiState.update {
            it.copy(
                accountToDisable = null,
                showDisableConfirmDialog = false
            )
        }
    }

    fun confirmDisableAccount() {
        val account = _uiState.value.accountToDisable ?: return
        safeLaunch {
            _uiState.update {
                it.copy(
                    disableAccountState = UiState.Loading,
                    showDisableConfirmDialog = false
                )
            }
            when (val result = disableFinancialAccountUseCase(account.id)) {
                is NetworkResult.Success -> _uiState.update { state ->
                    state.copy(
                        disableAccountState = UiState.Success(result.data),
                        accounts = state.accounts.map {
                            if (it.id == result.data.id) result.data else it
                        },
                        accountToDisable = null
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        disableAccountState = UiState.Error(
                            message = result.message,
                            throwable = result.throwable
                        ),
                        accountToDisable = null
                    )
                }
            }
        }
    }

    fun resetCreateAccountState() {
        _uiState.update { it.copy(createAccountState = UiState.Idle) }
    }

    fun resetDisableAccountState() {
        _uiState.update { it.copy(disableAccountState = UiState.Idle) }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                accountsState = UiState.Error(
                    message = throwable.message ?: "Error al cargar las cuentas",
                    throwable = throwable
                ),
                createAccountState = UiState.Error(
                    message = throwable.message ?: "Error al crear la cuenta",
                    throwable = throwable
                ),
                disableAccountState = UiState.Error(
                    message = throwable.message ?: "Error al inhabilitar la cuenta",
                    throwable = throwable
                )
            )
        }
    }
}