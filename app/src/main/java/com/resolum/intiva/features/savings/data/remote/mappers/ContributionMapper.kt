package com.resolum.intiva.features.savings.data.remote.mappers

import com.resolum.intiva.features.savings.data.remote.models.ContributionResponseDto
import com.resolum.intiva.features.savings.domain.models.GoalContribution
import com.resolum.intiva.features.savings.domain.models.Money

/**
 * Extension function to map a [ContributionResponseDto] to a [GoalContribution] domain model.
 *
 * Mirrors [CategoryMapper]: the data layer returns DTOs from Retrofit and the repository
 * maps them to domain entities before exposing them to use cases and ViewModels.
 */
fun ContributionResponseDto.toDomain() = GoalContribution(
    id = id,
    amountContributed = Money(amount = amount, currencyCode = currencyCode),
    contributorId = contributorId,
    contributedAt = contributedAt,
    savingGoalId = savingGoalId
)
