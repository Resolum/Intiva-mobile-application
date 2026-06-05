package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.models.SavingGoalOwnerType
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import javax.inject.Inject

/**
 * Fetches all saving goals for an account and returns only personal goals.
 */
class GetSavingGoalsUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(userId: Long): NetworkResult<List<SavingGoal>> =
        when (val result = repository.getSavingGoals(userId)) {
            is NetworkResult.Success -> NetworkResult.Success(
                result.data.filter { SavingGoalOwnerType.isPersonal(it.ownerType) }
            )
            is NetworkResult.Error -> result
        }
}
