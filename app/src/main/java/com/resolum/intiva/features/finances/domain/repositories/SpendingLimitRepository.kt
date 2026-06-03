package com.resolum.intiva.features.finances.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.CreateSpendingLimitRequest
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitAmountRequest
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitPeriodRequest

/**
 * Repository contract for spending limit operations.
 */
interface SpendingLimitRepository {

    suspend fun createSpendingLimit(
        request: CreateSpendingLimitRequest
    ): NetworkResult<SpendingLimit>

    suspend fun getSpendingLimitById(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit>

    suspend fun getSpendingLimits(
        ownerId: Long,
        ownerType: String? = null,
        targetType: String? = null,
        targetId: Long? = null
    ): NetworkResult<List<SpendingLimit>>

    suspend fun updateSpendingLimitAmount(
        spendingLimitId: Long,
        request: UpdateSpendingLimitAmountRequest
    ): NetworkResult<SpendingLimit>

    suspend fun updateSpendingLimitPeriod(
        spendingLimitId: Long,
        request: UpdateSpendingLimitPeriodRequest
    ): NetworkResult<SpendingLimit>

    suspend fun activateSpendingLimit(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit>

    suspend fun deactivateSpendingLimit(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit>
}
