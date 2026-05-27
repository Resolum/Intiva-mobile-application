package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.CategoryViewModel

/**
 * Composable function to display a grid of categories with loading and error states.
 *
 * @param viewModel The ViewModel that provides the UI state for the categories.
 * @param onCategorySelected Callback function to be invoked when a category is selected.
 */
@Composable
fun CategoryGrid(
    selectedCategory: Category?,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategorySelected: (Category) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "CATEGORÍA",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when (val state = uiState.categoriesState) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                if (uiState.categories.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes categorías aún",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                    ) {
                        items(uiState.categories) { category ->
                            CategoryItem(
                                category = category,
                                isSelected = category.id == selectedCategory?.id,
                                onClick = { onCategorySelected(category) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar categorías.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is UiState.Idle -> Unit
        }
    }
}