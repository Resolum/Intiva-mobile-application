package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.CreateSpendingLimitRequest
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.SpendingLimitOwnerType
import com.resolum.intiva.features.finances.domain.models.SpendingLimitTargetType
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import jakarta.inject.Inject
import java.math.BigDecimal

/**
 * Creates a spending limit after validating the domain request.
 */
class CreateSpendingLimitUseCase @Inject constructor(
    private val spendingLimitRepository: SpendingLimitRepository
) {
    suspend operator fun invoke(
        request: CreateSpendingLimitRequest
    ): NetworkResult<SpendingLimit> {
        if (request.ownerId <= 0L) {
            return NetworkResult.Error("Owner ID must be greater than zero")
        }
        if (!SpendingLimitOwnerType.isSupported(request.ownerType)) {
            return NetworkResult.Error("Invalid owner type")
        }
        if (!SpendingLimitTargetType.isSupported(request.targetType)) {
            return NetworkResult.Error("Invalid target type")
        }
        if (request.targetId <= 0L) {
            return NetworkResult.Error("Target ID must be greater than zero")
        }
        if (request.limitAmount <= BigDecimal.ZERO) {
            return NetworkResult.Error("Limit amount must be greater than zero")
        }
        if (request.currencyCode.isBlank()) {
            return NetworkResult.Error("Currency code is required")
        }
        if (request.startDate.isBlank() || request.endDate.isBlank()) {
            return NetworkResult.Error("Start date and end date are required")
        }

        return spendingLimitRepository.createSpendingLimit(request)
    }
}
