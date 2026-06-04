package com.resolum.intiva.features.finances.data.remote

import com.resolum.intiva.features.finances.data.remote.models.CreateSpendingLimitRequestDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitAmountRequestDto
import com.resolum.intiva.features.finances.data.remote.models.UpdateSpendingLimitPeriodRequestDto
import com.resolum.intiva.features.finances.data.remote.services.SpendingLimitService
import javax.inject.Inject

/**
 * Facade service for spending limit operations.
 */
class SpendingLimitFacadeService @Inject constructor(
    private val spendingLimitService: SpendingLimitService
) {

    suspend fun createSpendingLimit(request: CreateSpendingLimitRequestDto) =
        spendingLimitService.createSpendingLimit(request)

    suspend fun getSpendingLimitById(spendingLimitId: Long) =
        spendingLimitService.getSpendingLimitById(spendingLimitId)

    suspend fun getSpendingLimits(
        ownerId: Long,
        ownerType: String?,
        targetType: String?,
        targetId: Long?
    ) = spendingLimitService.getSpendingLimits(
        ownerId = ownerId,
        ownerType = ownerType,
        targetType = targetType,
        targetId = targetId
    )

    suspend fun updateSpendingLimitAmount(
        spendingLimitId: Long,
        request: UpdateSpendingLimitAmountRequestDto
    ) = spendingLimitService.updateSpendingLimitAmount(spendingLimitId, request)

    suspend fun updateSpendingLimitPeriod(
        spendingLimitId: Long,
        request: UpdateSpendingLimitPeriodRequestDto
    ) = spendingLimitService.updateSpendingLimitPeriod(spendingLimitId, request)

    suspend fun activateSpendingLimit(spendingLimitId: Long) =
        spendingLimitService.activateSpendingLimit(spendingLimitId)

    suspend fun deactivateSpendingLimit(spendingLimitId: Long) =
        spendingLimitService.deactivateSpendingLimit(spendingLimitId)
}
