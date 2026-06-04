package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import jakarta.inject.Inject

/**
 * Retrieves one spending limit by ID.
 */
class GetSpendingLimitByIdUseCase @Inject constructor(
    private val spendingLimitRepository: SpendingLimitRepository
) {
    suspend operator fun invoke(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit> {
        if (spendingLimitId <= 0L) {
            return NetworkResult.Error("Spending limit ID must be greater than zero")
        }

        return spendingLimitRepository.getSpendingLimitById(spendingLimitId)
    }
}
