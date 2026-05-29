package com.resolum.intiva.features.savings.data.remote.mappers

import com.resolum.intiva.features.savings.data.remote.models.SavingGoalResponseDto
import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * Extension function to map a [SavingGoalResponseDto] to a [SavingGoal] domain model.
 *
 * Normalizes the [ownerType] from the API response:
 * - Tab Personales: maps to "Individual"
 * - Tab Familiares: maps to "Group"
 */
fun SavingGoalResponseDto.toDomain() = SavingGoal(
    id = id,
    ownerType = when (ownerType?.lowercase()) {
        "individual", "user", "personal" -> "Individual"
        "family", "group" -> "Group"
        else -> ownerType ?: "Individual"
    },
    actorUserId = actorUserId,
    ownerId = ownerId ?: "",
    title = title ?: "",
    currentAmount = currentAmount ?: java.math.BigDecimal.ZERO,
    targetAmount = targetAmount ?: java.math.BigDecimal.ONE,
    currencyCode = currencyCode ?: "PEN",
    deadline = deadline ?: "",
    status = status ?: "INPROGRESS",
    categoryId = categoryId ?: 0L
)
