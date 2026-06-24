package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.lifecycle.SavedStateHandle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetFinancesAccountUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.UpdateFinancialAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class EditFinancialAccountUiState(
    val account: FinancialAccount? = null,
    val accountState: UiState<FinancialAccount> = UiState.Idle,
    val updateState: UiState<FinancialAccount> = UiState.Idle
)

@HiltViewModel
class EditFinancialAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFinancesAccountUseCase: GetFinancesAccountUseCase,
    private val updateFinancialAccountUseCase: UpdateFinancialAccountUseCase
) : BaseViewModel() {

    private val accountId: Long = savedStateHandle.get<String>("accountId")?.toLongOrNull() ?: -1L

    private val _uiState = MutableStateFlow(EditFinancialAccountUiState())
    val uiState: StateFlow<EditFinancialAccountUiState> = _uiState.asStateFlow()

    fun loadAccount() {
        safeLaunch {
            _uiState.update { it.copy(accountState = UiState.Loading) }
            val account = getFinancesAccountUseCase.getById(accountId)
            if (account != null) {
                _uiState.update {
                    it.copy(account = account, accountState = UiState.Success(account))
                }
            } else {
                _uiState.update {
                    it.copy(accountState = UiState.Error("Cuenta no encontrada"))
                }
            }
        }
    }

    fun updateFinancialAccount(name: String, isActive: Boolean) {
        safeLaunch {
            _uiState.update { it.copy(updateState = UiState.Loading) }
            when (val result = updateFinancialAccountUseCase(accountId, name, isActive)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            account = result.data,
                            updateState = UiState.Success(result.data)
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(updateState = UiState.Error(result.message, result.throwable))
                    }
                }
            }
        }
    }

    fun resetUpdateState() {
        _uiState.update { it.copy(updateState = UiState.Idle) }
    }
}
