package com.resolum.intiva.features.finances.presentation.transactions

import TransactionDateSection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.presentation.transactions.components.EmptyTransactionsContent

enum class FilterOption(val title: String, val type: TransactionType?) {
    ALL("Todos", null),
    INCOME("Ingresos", TransactionType.INCOME),
    EXPENSE("Gastos", TransactionType.EXPENSE)
}

/**
 * Composable function representing the transactions screen, which displays a list of financial transactions grouped by date.
 *
 * The screen includes a top app bar with the app name and a notifications icon, as well as filter chips to allow users to filter transactions by type (all, income, expenses).
 *
 * @param viewModel The ViewModel managing the state and logic for this screen, provided by Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(FilterOption.ALL) }

    LaunchedEffect(selectedFilter) {
        viewModel.getTransactionsByOwnerId(
            transactionType = selectedFilter.type
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Intiva",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = IntivaColors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = IntivaColors.IconPurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IntivaColors.BackgroundLavender
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterOption.entries.forEach { option ->
                    val isSelected = selectedFilter == option

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = option },
                        label = {
                            Text(
                                text = option.title,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            labelColor = IntivaColors.UnselectedChipText,
                            selectedContainerColor = IntivaColors.SelectedChip,
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val state = uiState.transactionsState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is UiState.Success -> {
                        val groupedTransactions = state.data

                        if (groupedTransactions.isEmpty()) {
                            EmptyTransactionsContent()
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    horizontal = 20.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                items(groupedTransactions) { group ->
                                    TransactionDateSection(group = group)
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        Text(
                            text = "Estado desconocido",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}