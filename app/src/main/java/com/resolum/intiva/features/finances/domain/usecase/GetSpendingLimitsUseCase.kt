package com.resolum.intiva.features.finances.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.SpendingLimitOwnerType
import com.resolum.intiva.features.finances.domain.models.SpendingLimitTargetType
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import jakarta.inject.Inject

/**
 * Retrieves spending limits for an owner, with optional target filters.
 */
class GetSpendingLimitsUseCase @Inject constructor(
    private val spendingLimitRepository: SpendingLimitRepository
) {
    suspend operator fun invoke(
        ownerId: Long,
        ownerType: String? = null,
        targetType: String? = null,
        targetId: Long? = null
    ): NetworkResult<List<SpendingLimit>> {
        if (ownerId <= 0L) {
            return NetworkResult.Error("Owner ID must be greater than zero")
        }
        if (ownerType != null && !SpendingLimitOwnerType.isSupported(ownerType)) {
            return NetworkResult.Error("Invalid owner type")
        }
        if (targetType != null && !SpendingLimitTargetType.isSupported(targetType)) {
            return NetworkResult.Error("Invalid target type")
        }
        if (targetId != null && targetId <= 0L) {
            return NetworkResult.Error("Target ID must be greater than zero")
        }
        if (targetId != null && targetType == null) {
            return NetworkResult.Error("Target type is required when target ID is provided")
        }

        return spendingLimitRepository.getSpendingLimits(
            ownerId = ownerId,
            ownerType = ownerType,
            targetType = targetType,
            targetId = targetId
        )
    }
}
