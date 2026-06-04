package com.resolum.intiva.features.savings.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.data.remote.SavingGoalFacadeService
import com.resolum.intiva.features.savings.data.remote.mappers.toDomain
import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Implementation of [SavingGoalRepository] that delegates all remote calls to [SavingGoalFacadeService].
 */
class SavingGoalRepositoryImpl @Inject constructor(
    private val savingGoalFacadeService: SavingGoalFacadeService
) : BaseRepository(), SavingGoalRepository {

    override suspend fun getSavingGoals(userId: Long): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getSavingGoals(userId).map { it.toDomain() }
    }

    override suspend fun getCompletedSavingGoals(userId: Long): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getCompletedSavingGoals(userId).map { it.toDomain() }
    }

    override suspend fun getGroupSavingGoals(groupId: String): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getGroupSavingGoals(groupId).map { it.toDomain() }
    }

    override suspend fun getSavingGoal(savingGoalId: Long): NetworkResult<SavingGoal> = safeCall {
        savingGoalFacadeService.getSavingGoal(savingGoalId).toDomain()
    }

    override suspend fun createSavingGoal(
        ownerType: String,
        actorUserId: Long,
        ownerId: String,
        title: String,
        targetAmount: BigDecimal,
        currencyCode: String,
        deadline: String,
        categoryId: Long,
        description: String
    ): NetworkResult<SavingGoal> = safeCall {
        savingGoalFacadeService.createSavingGoal(
            CreateSavingGoalRequestDto(
                ownerType = ownerType,
                actorUserId = actorUserId,
                ownerId = ownerId,
                title = title,
                targetAmount = targetAmount,
                currencyCode = currencyCode,
                deadline = deadline,
                categoryId = categoryId,
                description = description
            )
        ).toDomain()
    }

    /**
     * Posts the contribution, then re-fetches the goal so the UI gets the updated state.
     * The contributions endpoint returns 201 with an empty body ({}).
     */
    override suspend fun registerContribution(
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<SavingGoal> = safeCall {
        savingGoalFacadeService.registerContribution(
            savingGoalId,
            ContributionRequestDto(
                amount = amount,
                currencyCode = currencyCode,
                contributorId = contributorId
            )
        )
        savingGoalFacadeService.getSavingGoal(savingGoalId).toDomain()
    }

    override suspend fun completeGoal(savingGoalId: Long): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.completeGoal(savingGoalId)
    }

    override suspend fun uncompleteGoal(savingGoalId: Long): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.uncompleteGoal(savingGoalId)
    }

    override suspend fun deleteSavingGoal(savingGoalId: Long): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.deleteSavingGoal(savingGoalId)
    }

    override suspend fun updateSavingGoal(
        savingGoalId: Long,
        title: String?,
        description: String?,
        newTargetAmount: BigDecimal?
    ): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.updateSavingGoal(savingGoalId, title, description, newTargetAmount)
    }
}
