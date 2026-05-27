package com.resolum.intiva.features.savings.data.remote

import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.ContributionResponseDto
import com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
import com.resolum.intiva.features.savings.data.remote.models.SavingGoalResponseDto
import com.resolum.intiva.features.savings.data.remote.services.SavingGoalService
import javax.inject.Inject

/**
 * Facade service for saving goal operations.
 *
 * This class abstracts the underlying Retrofit service and provides a clean interface
 * for repository classes to interact with. It delegates all network calls to [SavingGoalService],
 * mirroring the pattern used by [CategoryFacadeService].
 */
class SavingGoalFacadeService @Inject constructor(
    private val savingGoalService: SavingGoalService
) {

    suspend fun getSavingGoals(accountId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getSavingGoals(accountId)

    suspend fun getCompletedSavingGoals(accountId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getCompletedSavingGoals(accountId)

    suspend fun getGroupSavingGoals(accountId: Long, groupId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getGroupSavingGoals(accountId, groupId)

    suspend fun createSavingGoal(
        accountId: Long,
        request: CreateSavingGoalRequestDto
    ): SavingGoalResponseDto = savingGoalService.createSavingGoal(accountId, request)

    /**
     * Retrieves the details of a saving goal.
     *
     * @param accountId    The ID of the account that owns the goal.
     * @param savingGoalId The ID of the saving goal.
     * @return A [SavingGoalResponseDto] with the goal's current data.
     */
    suspend fun getSavingGoal(accountId: Long, savingGoalId: Long): SavingGoalResponseDto =
        savingGoalService.getSavingGoal(accountId, savingGoalId)

    /**
     * Registers a monetary contribution to the specified saving goal.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     * @param request      The contribution details.
     */
    suspend fun registerContribution(
        accountId: Long,
        savingGoalId: Long,
        request: ContributionRequestDto
    ): ContributionResponseDto =
        savingGoalService.registerContribution(accountId, savingGoalId, request)

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     */
    suspend fun completeGoal(accountId: Long, savingGoalId: Long) =
        savingGoalService.completeGoal(accountId, savingGoalId)

    /**
     * Reverts a saving goal to UNCOMPLETED.
     *
     * @param accountId    The ID of the account.
     * @param savingGoalId The ID of the goal.
     */
    suspend fun uncompleteGoal(accountId: Long, savingGoalId: Long) =
        savingGoalService.uncompleteGoal(accountId, savingGoalId)
}
