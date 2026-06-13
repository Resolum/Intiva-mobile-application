package com.resolum.intiva.features.finances.presentation.transactions.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.transactions.DateRangeOption
import com.resolum.intiva.features.finances.presentation.transactions.FilterCategory
import com.resolum.intiva.features.finances.presentation.transactions.FilterOption
import com.resolum.intiva.features.finances.presentation.transactions.TransactionFilters
import com.resolum.intiva.features.finances.presentation.transactions.formatFilterDate
import com.resolum.intiva.features.finances.presentation.transactions.toDatePickerLocalDate
import com.resolum.intiva.features.finances.presentation.transactions.toDatePickerMillis
import com.resolum.intiva.features.finances.presentation.transactions.toDates
import java.time.LocalDate

private val SheetBackground = Color.White
private val SoftLavender = Color(0xFFF0EAF7)
private val SelectedLavender = Color(0xFFE8E6FF)
private val PurpleText = Color(0xFF251094)
private val MutedText = Color(0xFF5B5666)
private val SectionText = Color(0xFF817A8D)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFiltersSheet(
    filters: TransactionFilters,
    categories: List<FilterCategory>,
    onFiltersChange: (TransactionFilters) -> Unit,
    onClearCategories: () -> Unit,
    onClose: () -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SheetBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        FilterSheetHeader(onClose = onClose)

        FilterSectionTitle(text = "RANGO DE FECHAS")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DateRangeOption.entries.forEach { option ->
                FilterPill(
                    text = option.title,
                    selected = filters.dateRangeOption == option,
                    onClick = {
                        val (from, to) = option.toDates()
                        onFiltersChange(
                            filters.copy(
                                dateRangeOption = option,
                                fromDate = from,
                                toDate = to
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    compact = true
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DateInput(
                label = "Desde",
                date = filters.fromDate,
                onDateSelected = {
                    val nextToDate = filters.toDate?.takeIf { toDate -> !toDate.isBefore(it ?: toDate) }
                    onFiltersChange(
                        filters.copy(
                            fromDate = it,
                            toDate = nextToDate ?: it
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            )
            DateInput(
                label = "Hasta",
                date = filters.toDate,
                onDateSelected = {
                    val nextFromDate = filters.fromDate?.takeIf { fromDate -> !fromDate.isAfter(it ?: fromDate) }
                    onFiltersChange(
                        filters.copy(
                            fromDate = nextFromDate ?: it,
                            toDate = it
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        FilterSectionTitle(text = "TIPO DE MOVIMIENTO")
        MovementSelector(
            selected = filters.type,
            onSelected = { onFiltersChange(filters.copy(type = it, categoryIds = emptySet())) }
        )

        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterSectionTitle(text = "CATEGORIAS")

            Text(
                text = "Borrar selección",
                color = IntivaColors.PrimaryBrand,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onClearCategories() }
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categories.forEach { category ->
                val selected = category.id in filters.categoryIds
                FilterPill(
                    text = category.name,
                    selected = selected,
                    icon = category.icon,
                    onClick = {
                        val nextSelection = if (selected) {
                            filters.categoryIds - category.id
                        } else {
                            filters.categoryIds + category.id
                        }
                        onFiltersChange(filters.copy(categoryIds = nextSelection))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        ApplyFiltersButton(onApply = onApply)
    }
}

@Composable
private fun FilterSheetHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Filtros",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = IntivaColors.TextPrimary
            )
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(74.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(IntivaColors.PrimaryAction)
            )
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFFE9E2F0))
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar filtros",
                tint = Color(0xFF4A4653)
            )
        }
    }
}

@Composable
private fun MovementSelector(
    selected: FilterOption,
    onSelected: (FilterOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE6DFEB))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterOption.entries.forEach { option ->
            val isSelected = selected == option

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color(0xFFFFFBFE) else Color.Transparent)
                    .clickable { onSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.title,
                    color = if (isSelected) IntivaColors.PrimaryBrand else MutedText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FilterSectionTitle(text: String) {
    Text(
        text = text,
        color = SectionText,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun FilterPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    compact: Boolean = false
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .height(if (compact) 58.dp else 50.dp)
            .clip(shape)
            .background(if (selected) SelectedLavender else SoftLavender)
            .border(
                BorderStroke(
                    width = if (selected) 1.2.dp else 0.dp,
                    color = if (selected) IntivaColors.PrimaryBrand else Color.Transparent
                ),
                shape
            )
            .clickable { onClick() }
            .padding(horizontal = if (compact) 10.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = if (selected) PurpleText else MutedText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            color = if (selected) PurpleText else MutedText,
            fontSize = if (compact) 14.sp else 15.sp,
            lineHeight = if (compact) 17.sp else 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = if (compact) 2 else 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateInput(
    label: String,
    date: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = MutedText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF5F0FA))
                .border(1.dp, Color(0xFFD9CEE5), RoundedCornerShape(14.dp))
                .clickable { showPicker = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date?.formatFilterDate() ?: "Seleccionar",
                color = if (date == null) Color(0xFF827C8D) else IntivaColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = IntivaColors.PrimaryBrand,
                modifier = Modifier.size(23.dp)
            )
        }
    }

    if (showPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date?.toDatePickerMillis()
        )
        val selectedDate = datePickerState.selectedDateMillis
            ?.toDatePickerLocalDate()
            ?: date

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis?.toDatePickerLocalDate())
                        showPicker = false
                    }
                ) {
                    Text("Aceptar", color = IntivaColors.PrimaryBrand)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancelar", color = MutedText)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Selecciona una fecha",
                        color = MutedText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 24.dp, top = 18.dp)
                    )
                },
                headline = {
                    Text(
                        text = selectedDate?.formatFilterDate() ?: "Fecha",
                        color = IntivaColors.TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                    )
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = IntivaColors.PrimaryBrand,
                    selectedDayContentColor = Color.White,
                    todayDateBorderColor = IntivaColors.PrimaryAction,
                    todayContentColor = IntivaColors.PrimaryBrand,
                    currentYearContentColor = IntivaColors.PrimaryBrand,
                    selectedYearContainerColor = IntivaColors.PrimaryBrand
                )
            )
        }
    }
}

@Composable
private fun ApplyFiltersButton(onApply: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(IntivaColors.PrimaryAction)
            .clickable { onApply() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Aplicar Filtros",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = IntivaColors.TextPrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = IntivaColors.TextPrimary
            )
        }
    }
}
