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

    suspend fun getSavingGoals(userId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getSavingGoals(userId)

    suspend fun getCompletedSavingGoals(userId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getCompletedSavingGoals(userId)

    suspend fun getGroupSavingGoals(userId: Long, groupId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getGroupSavingGoals(userId, groupId)

    suspend fun createSavingGoal(
        userId: Long,
        request: CreateSavingGoalRequestDto
    ): SavingGoalResponseDto = savingGoalService.createSavingGoal(userId, request)

    /**
     * Retrieves the details of a saving goal.
     *
     * @param userId       The ID of the user that owns the goal.
     * @param savingGoalId The ID of the saving goal.
     * @return A [SavingGoalResponseDto] with the goal's current data.
     */
    suspend fun getSavingGoal(userId: Long, savingGoalId: Long): SavingGoalResponseDto =
        savingGoalService.getSavingGoal(userId, savingGoalId)

    /**
     * Registers a monetary contribution to the specified saving goal.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal.
     * @param request      The contribution details.
     */
    suspend fun registerContribution(
        userId: Long,
        savingGoalId: Long,
        request: ContributionRequestDto
    ): ContributionResponseDto =
        savingGoalService.registerContribution(userId, savingGoalId, request)

    /**
     * Marks a saving goal as COMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal.
     */
    suspend fun completeGoal(userId: Long, savingGoalId: Long) =
        savingGoalService.completeGoal(userId, savingGoalId)

    /**
     * Reverts a saving goal to UNCOMPLETED.
     *
     * @param userId       The ID of the user.
     * @param savingGoalId The ID of the goal.
     */
    suspend fun uncompleteGoal(userId: Long, savingGoalId: Long) =
        savingGoalService.uncompleteGoal(userId, savingGoalId)
}
