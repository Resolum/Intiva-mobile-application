package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitAmountRequest
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import jakarta.inject.Inject
import java.math.BigDecimal

/**
 * Updates the maximum amount of a spending limit.
 */
class UpdateSpendingLimitAmountUseCase @Inject constructor(
    private val spendingLimitRepository: SpendingLimitRepository
) {
    suspend operator fun invoke(
        spendingLimitId: Long,
        request: UpdateSpendingLimitAmountRequest
    ): NetworkResult<SpendingLimit> {
        if (spendingLimitId <= 0L) {
            return NetworkResult.Error("Spending limit ID must be greater than zero")
        }
        if (request.limitAmount <= BigDecimal.ZERO) {
            return NetworkResult.Error("Limit amount must be greater than zero")
        }
        if (request.currencyCode.isBlank()) {
            return NetworkResult.Error("Currency code is required")
        }

        return spendingLimitRepository.updateSpendingLimitAmount(spendingLimitId, request)
    }
}
