package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import javax.inject.Inject
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Evaluates whether a saving goal should be transitioned to COMPLETED or UNCOMPLETED
 * after a contribution is registered.
 *
 * Called from [ContributeToGoalViewModel] after every successful contribution:
 * - If [currentAmount] >= [targetAmount] → triggers [CompleteGoalUseCase]
 * - If the deadline has passed and target not reached → triggers [UncompleteGoalUseCase]
 * - Otherwise → no status change
 */
class GoalCompletionEvaluator @Inject constructor(
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val uncompleteGoalUseCase: UncompleteGoalUseCase
) {
    /**
     * Evaluates the goal's current state and triggers the appropriate use case if needed.
     *
     * @param userId The ID of the user.
     * @param goal   The refreshed [SavingGoal] after a contribution.
     * @return [CompletionResult] indicating the outcome of the evaluation.
     */
    suspend fun evaluate(userId: Long, goal: SavingGoal): CompletionResult {
        return when {
            // Target reached → mark as COMPLETED
            goal.currentAmount >= goal.targetAmount -> {
                val result = completeGoalUseCase(userId, goal.id)
                if (result is NetworkResult.Success) CompletionResult.Completed
                else CompletionResult.NoChange
            }
            // Deadline passed without reaching target → mark as UNCOMPLETED
            isDeadlinePassed(goal.deadline) && goal.currentAmount < goal.targetAmount -> {
                val result = uncompleteGoalUseCase(userId, goal.id)
                if (result is NetworkResult.Success) CompletionResult.Uncompleted
                else CompletionResult.NoChange
            }
            // Goal is still in progress
            else -> CompletionResult.NoChange
        }
    }

    /**
     * Checks whether the goal's [deadline] date has passed relative to today.
     * Returns false if the date cannot be parsed (fail-safe).
     */
    private fun isDeadlinePassed(deadline: String): Boolean {
        return try {
            // The deadline from the API is an ISO-8601 datetime; take just the date portion
            val deadlineDate = LocalDate.parse(deadline.take(10))
            deadlineDate.isBefore(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }

    /** Represents the outcome of [GoalCompletionEvaluator.evaluate]. */
    sealed class CompletionResult {
        /** The goal was marked COMPLETED. */
        data object Completed : CompletionResult()

        /** The goal was marked UNCOMPLETED. */
        data object Uncompleted : CompletionResult()

        /** No status change was required. */
        data object NoChange : CompletionResult()
    }
}
