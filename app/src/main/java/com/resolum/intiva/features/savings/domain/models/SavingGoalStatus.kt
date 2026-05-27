package com.resolum.intiva.features.savings.domain.models

/**
 * Enum representing the possible lifecycle statuses of a saving goal.
 *
 * - [INPROGRESS]  — The goal is active and contributions are still being accepted.
 * - [COMPLETED]   — The target amount was reached; goal is closed successfully.
 * - [UNCOMPLETED] — The deadline passed without reaching the target amount.
 */
enum class SavingGoalStatus {
    INPROGRESS,
    COMPLETED,
    UNCOMPLETED;

    companion object {
        /**
         * Parses a raw API string into a [SavingGoalStatus].
         * Defaults to [INPROGRESS] if the value is unknown.
         */
        fun from(value: String): SavingGoalStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: INPROGRESS
    }
}
