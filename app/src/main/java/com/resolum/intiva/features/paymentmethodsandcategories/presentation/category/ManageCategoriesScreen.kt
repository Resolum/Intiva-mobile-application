package com.resolum.intiva.features.paymentmethodsandcategories.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryCreateScreen
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.category.components.CategoryIcon

private val BrandPurple = Color(0xFF534AB7)
private val AccentGreen = Color(0xFFCDEB45)
private val NeutralGray = Color(0xFF78767E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorías",
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = BrandPurple
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationsNone,
                            contentDescription = "Notificaciones",
                            tint = BrandPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IntivaColors.BackgroundLavender
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.resetCreateForm()
                    showCreateSheet = true
                },
                containerColor = AccentGreen,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear categoría",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        containerColor = IntivaColors.BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Gestionar Categorías",
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary,
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Personaliza cómo organizas tus movimientos.",
                color = NeutralGray,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(28.dp))

            when (val state = uiState.categoriesState) {
                is UiState.Idle,
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BrandPurple)
                    }
                }

                is UiState.Error -> {
                    CategoryErrorContent(
                        message = state.message,
                        onRetry = { viewModel.getCategories() }
                    )
                }

                is UiState.Success -> {
                    if (uiState.categories.isEmpty()) {
                        EmptyCategoriesContent()
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.categories, key = { it.id }) { category ->
                                ManageCategoryItem(category = category)
                            }

                            item {
                                Spacer(modifier = Modifier.height(96.dp))
                            }
                        }
                    }
                }
            }
        }

        if (showCreateSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCreateSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                CategoryCreateScreen(
                    uiState = uiState,
                    onNameChange = viewModel::onNameChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onIconSelected = viewModel::onIconSelected,
                    onColorSelected = viewModel::onColorSelected,
                    onCreateCategory = {
                        viewModel.createCategory(
                            onSuccess = {
                                showCreateSheet = false
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ManageCategoryItem(
    category: Category
) {
    val backgroundColor = runCatching {
        Color(android.graphics.Color.parseColor(category.color))
    }.getOrDefault(Color(0xFFD8CFF7))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CategoryIcon(iconName = category.icon)
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    color = IntivaColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.description.ifBlank { "Sin descripción" },
                    color = NeutralGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar categoría",
                    tint = NeutralGray
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar categoría",
                    tint = NeutralGray
                )
            }
        }
    }
}

@Composable
private fun CategoryErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No se pudieron cargar las categorías.",
            color = IntivaColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            color = NeutralGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRetry) {
            Text(
                text = "Reintentar",
                color = BrandPurple,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyCategoriesContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No tienes categorías registradas.",
            color = NeutralGray
        )
    }
}