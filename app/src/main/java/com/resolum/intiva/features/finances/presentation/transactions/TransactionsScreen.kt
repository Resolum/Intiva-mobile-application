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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.finances.presentation.transactions.components.AppliedFiltersHeader
import com.resolum.intiva.features.finances.presentation.transactions.components.EmptyTransactionsContent
import com.resolum.intiva.features.finances.presentation.transactions.components.SyncStatusBanner
import com.resolum.intiva.features.finances.presentation.transactions.components.TransactionFiltersSheet
import com.resolum.intiva.features.finances.presentation.transactions.components.iconForTransactionCategory
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.CategoryViewModel
import com.resolum.intiva.features.profiles.presentation.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onNavigateToTransactionDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()
    val categoryUiState by categoryViewModel.uiState.collectAsState()
    var appliedFilters by remember { mutableStateOf(TransactionFilters()) }
    var draftFilters by remember { mutableStateOf(appliedFilters) }
    var showFilters by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val categories = remember(categoryUiState.categories, appliedFilters.type) {
        val loadedCategories = categoryUiState.categories
            .filter { it.isActive }
            .map { category ->
                FilterCategory(
                    id = category.id,
                    name = category.name,
                    icon = iconForTransactionCategory(category.name)
                )
            }

        categoriesForFilter(
            type = appliedFilters.type,
            loadedCategories = loadedCategories
        )
    }

    val draftCategories = remember(categoryUiState.categories, draftFilters.type) {
        val loadedCategories = categoryUiState.categories
            .filter { it.isActive }
            .map { category ->
                FilterCategory(
                    id = category.id,
                    name = category.name,
                    icon = iconForTransactionCategory(category.name)
                )
            }

        categoriesForFilter(
            type = draftFilters.type,
            loadedCategories = loadedCategories
        )
    }

    LaunchedEffect(draftFilters.type, showFilters) {
        if (showFilters && draftFilters.type != appliedFilters.type) {
            val categoryType = draftFilters.type.type ?: TransactionType.EXPENSE
            categoryViewModel.getCategories(type = categoryType.name)
        }
    }

    LaunchedEffect(appliedFilters.type) {
        viewModel.getTransactionsByOwnerId(
            transactionType = appliedFilters.type.type
        )

        val categoryType = appliedFilters.type.type ?: TransactionType.EXPENSE
        categoryViewModel.getCategories(type = categoryType.name)
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val profile = (profileUiState.profileState as? UiState.Success)?.data
                        val avatarUrl = profile?.avatarUrl?.ifEmpty { null }
                        AsyncImage(
                            model = avatarUrl
                                ?: "https://res.cloudinary.com/dcppsmlzd/image/upload/v1781121388/avatar_default_kf0yvc.png",
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = profile?.name?.split(" ")?.firstOrNull() ?: "Intiva",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = IntivaColors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            draftFilters = appliedFilters
                            showFilters = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = IntivaColors.IconPurple,
                            modifier = Modifier.size(26.dp)
                        )
                    }
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
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            AppliedFiltersHeader(
                filters = appliedFilters,
                onFilterSelected = { option ->
                    appliedFilters = appliedFilters.copy(
                        type = option,
                        categoryIds = emptySet()
                    )
                }
            )

            SyncStatusBanner(
                summary = uiState.syncStatusSummary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val state = uiState.transactionsState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = IntivaColors.PrimaryBrand
                        )
                    }

                    is UiState.Success -> {
                        val groupedTransactions = remember(state.data, appliedFilters) {
                            state.data.filterWith(appliedFilters)
                        }

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
                                    TransactionDateSection(
                                        group = group,
                                        onTransactionClick = { transaction ->
                                            onNavigateToTransactionDetail(transaction.id)
                                        }
                                    )
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

        if (showFilters) {
            ModalBottomSheet(
                onDismissRequest = { showFilters = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                containerColor = Color.White
            ) {
                TransactionFiltersSheet(
                    filters = draftFilters,
                    categories = draftCategories,
                    onFiltersChange = { draftFilters = it },
                    onClearCategories = {
                        draftFilters = draftFilters.copy(categoryIds = emptySet())
                    },
                    onClose = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showFilters = false
                        }
                    },
                    onApply = {
                        appliedFilters = draftFilters
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showFilters = false
                        }
                    }
                )
            }
        }
    }
}

private fun categoriesForFilter(
    type: FilterOption,
    loadedCategories: List<FilterCategory>
): List<FilterCategory> {
    return when (type) {
        FilterOption.INCOME -> loadedCategories
            .mergeWithDefaults(defaultIncomeCategories())
            .take(3)
        FilterOption.EXPENSE -> loadedCategories
            .mergeWithDefaults(defaultExpenseCategories())
            .take(3)
        FilterOption.ALL -> defaultIncomeCategories().take(3) + defaultExpenseCategories().take(3)
    }
}

private fun List<FilterCategory>.mergeWithDefaults(
    defaults: List<FilterCategory>
): List<FilterCategory> {
    val names = map { it.name.normalizedCategoryName() }.toSet()
    return this + defaults.filter { it.name.normalizedCategoryName() !in names }
}

private fun String.normalizedCategoryName(): String {
    return lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
}

private fun defaultIncomeCategories() = listOf(
    FilterCategory(1, "Salario", Icons.Default.Work),
    FilterCategory(2, "Freelance", Icons.Default.LaptopMac),
    FilterCategory(3, "Negocio", Icons.Default.Business)
)

private fun defaultExpenseCategories() = listOf(
    FilterCategory(7, "Alimentacion", Icons.Default.Restaurant),
    FilterCategory(8, "Transporte", Icons.Default.DirectionsCar),
    FilterCategory(9, "Salud", Icons.Default.FavoriteBorder)
)
