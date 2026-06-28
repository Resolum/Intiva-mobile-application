package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.finances.domain.models.TransactionWithDesignResponse
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

data class AccountDetailUiState(
    val account: FinancialAccount? = null,
    val accountState: UiState<FinancialAccount> = UiState.Idle,
    val recentTransactions: List<TransactionWithDesignResponse> = emptyList(),
    val transactionsState: UiState<List<TransactionWithDesignResponse>> = UiState.Idle
)
