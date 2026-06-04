package com.resolum.intiva.features.savings.domain.models

/**
 * Represents a single monetary contribution made to a specific saving goal.
 *
 * @property id The unique identifier for this contribution.
 * @property amountContributed The monetary amount contributed.
 * @property contributorId The ID of the user who made the contribution.
 * @property contributedAt The timestamp when the contribution was made.
 * @property savingGoalId The ID of the saving goal this contribution belongs to.
 */
data class GoalContribution(
    val id: Long,
    val amountContributed: Money,
    val contributorId: Long,
    val contributedAt: String,
    val savingGoalId: Long
)
