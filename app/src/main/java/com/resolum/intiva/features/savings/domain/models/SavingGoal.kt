package com.resolum.intiva.features.savings.domain.models

import java.math.BigDecimal

/**
 * Domain model representing a saving goal.
 *
 * This is the clean domain entity used throughout the application.
 * It is decoupled from any network DTOs or database entities.
 *
 * @property id            Unique identifier of the saving goal.
 * @property ownerType     Type of owner: "USER" for personal goals, "FAMILY" for shared goals.
 * @property actorUserId   ID of the user who created/manages the goal.
 * @property ownerId       ID of the owner entity (user or family group).
 * @property title         Display name of the saving goal.
 * @property currentAmount Amount saved so far.
 * @property targetAmount  Total amount required to achieve the goal.
 * @property currencyCode  ISO 4217 currency code (e.g. "USD", "PEN").
 * @property deadline      ISO-8601 date string for the goal's target deadline.
 * @property status        Current goal status: "INPROGRESS", "COMPLETED", or "UNCOMPLETED".
 * @property categoryId    ID of the category this goal belongs to.
 */
data class SavingGoal(
    val id: Long,
    val ownerType: String,
    val actorUserId: Long,
    val ownerId: String,
    val title: String,
    val currentAmount: BigDecimal,
    val targetAmount: BigDecimal,
    val currencyCode: String,
    val deadline: String,
    val status: String,
    val categoryId: Long
)
