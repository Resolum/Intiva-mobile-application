package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.time.LocalDate

/**
 * A selectable family group shown in the create-goal form.
 */
data class FamilyGroupOption(
    val groupId: Long,
    val label: String
)

/**
 * UI state for [SavingsGoalCreateScreen]: form fields, validation errors, and submission status.
 */
data class SavingsGoalCreateUiState(
    val accountId: Long? = null,
    val title: String = "",
    val targetAmountInput: String = "",
    val currencyCode: String = "PEN",
    val deadline: LocalDate? = null,
    val isFamilyGoal: Boolean = false,
    val selectedGroupId: Long? = null,
    val availableGroups: List<FamilyGroupOption> = emptyList(),
    val categoryId: Long = 1L,
    val titleError: String? = null,
    val targetAmountError: String? = null,
    val deadlineError: String? = null,
    val groupError: String? = null,
    val isLoading: Boolean = false,
    val submitError: String? = null,
    val createdGoal: SavingGoal? = null
) {
    /** True when all required fields pass validation (used to enable the save button). */
    val isFormValid: Boolean
        get() = title.isNotBlank() &&
            targetAmountInput.toBigDecimalOrNull()?.let { it > java.math.BigDecimal.ZERO } == true &&
            deadline != null &&
            deadline.isAfter(LocalDate.now()) &&
            (!isFamilyGoal || selectedGroupId != null)

    val selectedGroupLabel: String
        get() = availableGroups.firstOrNull { it.groupId == selectedGroupId }?.label
            ?: "Seleccionar grupo"
}
