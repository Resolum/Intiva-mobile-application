package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/** Creates a new saving goal via POST /saving-goals. */
class CreateSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(
        ownerType: String,
        actorUserId: Long,
        ownerId: String,
        title: String,
        targetAmount: BigDecimal,
        currencyCode: String,
        deadline: String,
        categoryId: Long,
        description: String = ""
    ): NetworkResult<SavingGoal> = repository.createSavingGoal(
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
}
