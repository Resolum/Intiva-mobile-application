package com.resolum.intiva.features.finances.presentation.spendinglimits.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryGrid
import com.resolum.intiva.features.shared.domain.model.OwnerType

@Composable
fun SpendingLimitCategorySelector(
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    CategoryGrid(
        selectedCategory = selectedCategory,
        onCategorySelected = onCategorySelected,
        modifier = modifier,
        ownerType = OwnerType.INDIVIDUAL.name,
        type = TransactionType.EXPENSE.name
    )
}
