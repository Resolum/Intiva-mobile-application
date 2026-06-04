package com.resolum.intiva.features.finances.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.data.remote.SpendingLimitFacadeService
import com.resolum.intiva.features.finances.data.remote.mappers.toDomain
import com.resolum.intiva.features.finances.data.remote.mappers.toDto
import com.resolum.intiva.features.finances.domain.models.CreateSpendingLimitRequest
import com.resolum.intiva.features.finances.domain.models.SpendingLimit
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitAmountRequest
import com.resolum.intiva.features.finances.domain.models.UpdateSpendingLimitPeriodRequest
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import javax.inject.Inject

/**
 * Remote implementation of [SpendingLimitRepository].
 */
class SpendingLimitRepositoryImpl @Inject constructor(
    private val spendingLimitFacadeService: SpendingLimitFacadeService
) : BaseRepository(), SpendingLimitRepository {

    override suspend fun createSpendingLimit(
        request: CreateSpendingLimitRequest
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService.createSpendingLimit(request.toDto()).toDomain()
    }

    override suspend fun getSpendingLimitById(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService.getSpendingLimitById(spendingLimitId).toDomain()
    }

    override suspend fun getSpendingLimits(
        ownerId: Long,
        ownerType: String?,
        targetType: String?,
        targetId: Long?
    ): NetworkResult<List<SpendingLimit>> = safeCall {
        spendingLimitFacadeService.getSpendingLimits(
            ownerId = ownerId,
            ownerType = ownerType,
            targetType = targetType,
            targetId = targetId
        ).data.map { it.toDomain() }
    }

    override suspend fun updateSpendingLimitAmount(
        spendingLimitId: Long,
        request: UpdateSpendingLimitAmountRequest
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService
            .updateSpendingLimitAmount(spendingLimitId, request.toDto())
            .toDomain()
    }

    override suspend fun updateSpendingLimitPeriod(
        spendingLimitId: Long,
        request: UpdateSpendingLimitPeriodRequest
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService
            .updateSpendingLimitPeriod(spendingLimitId, request.toDto())
            .toDomain()
    }

    override suspend fun activateSpendingLimit(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService.activateSpendingLimit(spendingLimitId).toDomain()
    }

    override suspend fun deactivateSpendingLimit(
        spendingLimitId: Long
    ): NetworkResult<SpendingLimit> = safeCall {
        spendingLimitFacadeService.deactivateSpendingLimit(spendingLimitId).toDomain()
    }
}
