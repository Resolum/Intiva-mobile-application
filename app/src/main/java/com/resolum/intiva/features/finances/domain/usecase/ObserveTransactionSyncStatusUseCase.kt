package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import javax.inject.Inject

class ObserveTransactionSyncStatusUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke() = transactionRepository.observeSyncStatusSummary()
}
