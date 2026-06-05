package com.resolum.intiva.features.savings.data.remote

import com.resolum.intiva.features.savings.data.remote.models.ContributionRequestDto
import com.resolum.intiva.features.savings.data.remote.models.CreateSavingGoalRequestDto
import com.resolum.intiva.features.savings.data.remote.models.SavingGoalResponseDto
import com.resolum.intiva.features.savings.data.remote.models.UpdateSavingGoalRequestDto
import com.resolum.intiva.features.savings.data.remote.services.SavingGoalService
import javax.inject.Inject

/**
 * Facade service for saving goal operations.
 *
 * Delegates all network calls to [SavingGoalService], following the same pattern
 * used by [CategoryFacadeService].
 */
class SavingGoalFacadeService @Inject constructor(
    private val savingGoalService: SavingGoalService
) {

    suspend fun getSavingGoals(userId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getSavingGoals(userId)

    suspend fun getCompletedSavingGoals(userId: Long): List<SavingGoalResponseDto> =
        savingGoalService.getCompletedSavingGoals(userId)

    /** [groupId] is a String to match the API path parameter type. */
    suspend fun getGroupSavingGoals(groupId: String): List<SavingGoalResponseDto> =
        savingGoalService.getGroupSavingGoals(groupId)

    suspend fun getSavingGoal(savingGoalId: Long): SavingGoalResponseDto =
        savingGoalService.getSavingGoal(savingGoalId)

    suspend fun createSavingGoal(request: CreateSavingGoalRequestDto): SavingGoalResponseDto =
        savingGoalService.createSavingGoal(request)

    suspend fun registerContribution(savingGoalId: Long, request: ContributionRequestDto) =
        savingGoalService.registerContribution(savingGoalId, request)

    suspend fun completeGoal(savingGoalId: Long) =
        savingGoalService.completeGoal(savingGoalId)

    suspend fun uncompleteGoal(savingGoalId: Long) =
        savingGoalService.uncompleteGoal(savingGoalId)

    suspend fun deleteSavingGoal(savingGoalId: Long) =
        savingGoalService.deleteSavingGoal(savingGoalId)

    suspend fun updateSavingGoal(
        savingGoalId: Long,
        title: String?,
        description: String?,
        newTargetAmount: java.math.BigDecimal?
    ) = savingGoalService.updateSavingGoal(
        savingGoalId,
        UpdateSavingGoalRequestDto(title, description, newTargetAmount)
    )
}
