package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Creates a new saving goal and returns the created entity with its server-assigned ID.
 */
class CreateSavingGoalUseCase @Inject constructor(
    private val repository: SavingGoalRepository
) {
    suspend operator fun invoke(
        userId: Long,
        title: String,
        targetAmount: BigDecimal,
        currencyCode: String,
        deadline: String,
        ownerType: String,
        categoryId: Long,
        description: String = ""
    ): NetworkResult<SavingGoal> = repository.createSavingGoal(
        userId = userId,
        title = title,
        targetAmount = targetAmount,
        currencyCode = currencyCode,
        deadline = deadline,
        ownerType = ownerType,
        categoryId = categoryId,
        description = description
    )
}
