package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

/**
 * Data class representing the UI state for the category screen, including the list of categories
 * and the current loading/error state.
 *
 * @property categories The list of categories to display in the UI.
 * @property categoriesState The current state of loading or error for fetching categories.
 */
data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val categoriesState: UiState<List<Category>> = UiState.Idle
)
