package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing the state of the Category screen, including fetching categories and handling UI state.
 *
 * This ViewModel interacts with the GetCategoriesUseCase to retrieve category data and updates the UI state accordingly.
 * It also handles errors that may occur during data fetching and provides a clean interface for the UI layer to observe changes.
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun getCategories() {
        safeLaunch {
            _uiState.update { it.copy(categoriesState = UiState.Loading) }
            when (val result = getCategoriesUseCase()) {
                is NetworkResult.Success -> _uiState.update {
                    it.copy(
                        categories = result.data,
                        categoriesState = UiState.Success(result.data)
                    )
                }
                is NetworkResult.Error -> _uiState.update {
                    it.copy(
                        categoriesState = UiState.Error(
                            message = result.message,
                            throwable = result.throwable
                        )
                    )
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                categoriesState = UiState.Error(
                    message = throwable.message ?: "An error occurred while fetching categories",
                    throwable = throwable
                )
            )
        }
    }
}