package com.resolum.intiva.features.savings.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.time.LocalDate
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * Evaluates whether a saving goal should transition to COMPLETED or UNCOMPLETED
 * after a contribution is registered.
 */
class GoalCompletionEvaluator @Inject constructor(
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val uncompleteGoalUseCase: UncompleteGoalUseCase
) {
    suspend fun evaluate(goal: SavingGoal): CompletionResult {
        return when {
            goal.currentAmount >= goal.targetAmount -> {
                val result = completeGoalUseCase(goal.id)
                if (result is NetworkResult.Success || result is NetworkResult.Error) CompletionResult.Completed
                else CompletionResult.NoChange
            }
            isDeadlinePassed(goal.deadline) && goal.currentAmount < goal.targetAmount -> {
                val result = uncompleteGoalUseCase(goal.id)
                if (result is NetworkResult.Success) CompletionResult.Uncompleted
                else CompletionResult.NoChange
            }
            else -> CompletionResult.NoChange
        }
    }

    private fun isDeadlinePassed(deadline: String): Boolean {
        return try {
            LocalDate.parse(deadline.take(10)).isBefore(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }

    sealed class CompletionResult {
        data object Completed : CompletionResult()
        data object Uncompleted : CompletionResult()
        data object NoChange : CompletionResult()
    }
}
