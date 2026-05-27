package com.resolum.intiva.features.savings.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.data.remote.SavingGoalFacadeService
import com.resolum.intiva.features.savings.data.remote.mappers.toDomain
import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Implementation of [SavingGoalRepository] that delegates all remote calls to [SavingGoalFacadeService].
 *
 * Uses [BaseRepository.safeCall] to wrap every network call in a [NetworkResult], providing
 * consistent error handling across the feature (HTTP errors, IO errors, unknown exceptions).
 */
class SavingGoalRepositoryImpl @Inject constructor(
    private val savingGoalFacadeService: SavingGoalFacadeService
) : BaseRepository(), SavingGoalRepository {

    override suspend fun getSavingGoals(accountId: Long): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getSavingGoals(accountId).map { it.toDomain() }
    }

    override suspend fun getCompletedSavingGoals(accountId: Long): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getCompletedSavingGoals(accountId).map { it.toDomain() }
    }

    override suspend fun getGroupSavingGoals(
        accountId: Long,
        groupId: Long
    ): NetworkResult<List<SavingGoal>> = safeCall {
        savingGoalFacadeService.getGroupSavingGoals(accountId, groupId).map { it.toDomain() }
    }

    override suspend fun createSavingGoal(
        accountId: Long,
        title: String,
        targetAmount: BigDecimal,
        currencyCode: String,
        deadline: String,
        ownerType: String,
        categoryId: Long,
        description: String
    ): NetworkResult<SavingGoal> = safeCall {
        savingGoalFacadeService.createSavingGoal(
            accountId = accountId,
            request = CreateSavingGoalRequestDto(
                title = title,
                targetAmount = targetAmount,
                currencyCode = currencyCode,
                deadline = deadline,
                ownerType = ownerType,
                categoryId = categoryId,
                description = description
            )
        ).toDomain()
    }

    /**
     * Fetches a saving goal from the remote API and maps it to the domain model.
     *
     * @param accountId    The ID of the account that owns the goal.
     * @param savingGoalId The ID of the saving goal to fetch.
     * @return A [NetworkResult] containing the [SavingGoal] domain model on success.
     */
    override suspend fun getSavingGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<SavingGoal> = safeCall {
        savingGoalFacadeService.getSavingGoal(accountId, savingGoalId).toDomain()
    }

    /**
     * Registers a contribution to a saving goal. The amount must be validated as > 0
     * before reaching this layer (enforced in [RegisterContributionUseCase]).
     *
     * @param accountId     The ID of the account.
     * @param savingGoalId  The ID of the goal.
     * @param amount        The monetary amount to contribute.
     * @param currencyCode  ISO 4217 currency code.
     * @param contributorId ID of the contributing user.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    override suspend fun registerContribution(
        accountId: Long,
        savingGoalId: Long,
        amount: BigDecimal,
        currencyCode: String,
        contributorId: Long
    ): NetworkResult<GoalContribution> = safeCall {
        val requestDto = ContributionRequestDto(
            amount = amount,
            currencyCode = currencyCode,
            contributorId = contributorId
        )
        savingGoalFacadeService
            .registerContribution(accountId, savingGoalId, requestDto)
            .toDomain()
    }

    /**
     * Calls the PATCH /complete endpoint to mark the goal as COMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    override suspend fun completeGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.completeGoal(accountId, savingGoalId)
    }

    /**
     * Calls the PATCH /uncomplete endpoint to revert the goal to UNCOMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     * @return A [NetworkResult] wrapping [Unit] on success.
     */
    override suspend fun uncompleteGoal(
        accountId: Long,
        savingGoalId: Long
    ): NetworkResult<Unit> = safeCall {
        savingGoalFacadeService.uncompleteGoal(accountId, savingGoalId)
    }
}
