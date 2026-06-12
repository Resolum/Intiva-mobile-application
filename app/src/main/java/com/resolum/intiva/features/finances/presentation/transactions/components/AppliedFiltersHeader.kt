package com.resolum.intiva.features.finances.presentation.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.transactions.FilterOption
import com.resolum.intiva.features.finances.presentation.transactions.TransactionFilters

@Composable
fun AppliedFiltersHeader(
    filters: TransactionFilters,
    onFilterSelected: (FilterOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterOption.entries.forEach { option ->
            val isSelected = filters.type == option

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(option) },
                label = {
                    Text(
                        text = option.title,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                },
                shape = RoundedCornerShape(50.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    labelColor = IntivaColors.UnselectedChipText,
                    selectedContainerColor = IntivaColors.PrimaryBrand,
                    selectedLabelColor = IntivaColors.SelectedChipText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = IntivaColors.UnselectedChipBorder,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp
                )
            )
        }
    }
}
