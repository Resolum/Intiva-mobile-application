package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.CreateCategoryUseCase
import com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun getCategories(ownerType: String = "", type: String = "") {
        safeLaunch {
            _uiState.update { it.copy(categoriesState = UiState.Loading) }
            when (val result = getCategoriesUseCase(ownerType, type)) {
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

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(
                name = value,
                nameError = if (value.isBlank()) "El nombre de la categoría es obligatorio" else null
            )
        }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onIconSelected(icon: String) {
        _uiState.update { it.copy(selectedIcon = icon) }
    }

    fun onColorSelected(color: String) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun resetCreateForm() {
        _uiState.update {
            it.copy(
                name = "",
                description = "",
                selectedIcon = "shopping-cart",
                selectedColor = "#D8CFF7",
                nameError = null,
                createCategoryState = UiState.Idle
            )
        }
    }

    fun createCategory(onSuccess: () -> Unit = {}) {
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update {
                it.copy(nameError = "El nombre de la categoría es obligatorio")
            }
            return
        }

        safeLaunch {
            _uiState.update { it.copy(createCategoryState = UiState.Loading) }

            when (
                val result = createCategoryUseCase(
                    name = state.name.trim(),
                    description = state.description.trim(),
                    color = state.selectedColor,
                    icon = state.selectedIcon
                )
            ) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            createCategoryState = UiState.Success(result.data),
                            name = "",
                            description = "",
                            selectedIcon = "shopping-cart",
                            selectedColor = "#D8CFF7",
                            nameError = null
                        )
                    }
                    getCategories()
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            createCategoryState = UiState.Error(
                                message = result.message,
                                throwable = result.throwable
                            )
                        )
                    }
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                createCategoryState = UiState.Error(
                    message = throwable.message ?: "Ocurrió un error al crear la categoría",
                    throwable = throwable
                )
            )
        }
    }
}