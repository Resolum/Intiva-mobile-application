package com.resolum.intiva.features.finances.presentation.transactions

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.finances.domain.models.Transaction
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate

/**
 * Represents the UI state for a transaction-related screen in the finances feature.
 *
 * @property state The current state of the transaction data, which can be idle, loading, success, or error.
 * @property transactionsState The current state of the list of transactions grouped by date, which can also be idle, loading, success, or error.
 * @property navigateBack A flag indicating whether the UI should navigate back to the previous screen.
 */
data class TransactionUiState(
    val state: UiState<Transaction> = UiState.Idle,
    val transactionsState: UiState<List<TransactionGroupByDate>> = UiState.Idle,
    val navigateBack: Boolean = false
)
