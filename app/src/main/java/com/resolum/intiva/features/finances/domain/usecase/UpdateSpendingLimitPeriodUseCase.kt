package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitPeriodRequest
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import jakarta.inject.Inject

/**
 * Updates the active date range of a spending limit.
 */
class UpdateSpendingLimitPeriodUseCase @Inject constructor(
    private val spendingLimitRepository: SpendingLimitRepository
) {
    suspend operator fun invoke(
        spendingLimitId: Long,
        request: UpdateSpendingLimitPeriodRequest
    ): NetworkResult<SpendingLimit> {
        if (spendingLimitId <= 0L) {
            return NetworkResult.Error("Spending limit ID must be greater than zero")
        }
        if (request.startDate.isBlank() || request.endDate.isBlank()) {
            return NetworkResult.Error("Start date and end date are required")
        }

        return spendingLimitRepository.updateSpendingLimitPeriod(spendingLimitId, request)
    }
}
