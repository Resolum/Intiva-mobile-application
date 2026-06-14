package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

/**
 * Data class representing the UI state for the financial account screen, including the list of financial accounts
 * and the current loading/error state.
 *
 * @property accounts The list of financial accounts to display in the UI.
 * @property accountsState The current state of loading or error for fetching financial accounts.
 */
data class FinancialAccountUiState(
    val accounts: List<FinancialAccount> = emptyList(),
    val accountsState: UiState<List<FinancialAccount>> = UiState.Idle,
    val createAccountState: UiState<FinancialAccount> = UiState.Idle,
    val disableAccountState: UiState<FinancialAccount> = UiState.Idle,
    val accountToDisable: FinancialAccount? = null,
    val showDisableConfirmDialog: Boolean = false
)
