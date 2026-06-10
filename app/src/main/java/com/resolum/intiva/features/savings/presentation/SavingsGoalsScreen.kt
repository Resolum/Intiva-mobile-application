package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.profiles.presentation.ProfileViewModel
import com.resolum.intiva.features.savings.domain.models.SavingGoal

/**
 * Primary savings goals dashboard list screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreate: (accountId: Long) -> Unit,
    onNavigateToDetail: (accountId: Long, goalId: Long) -> Unit,
    onNavigateToEdit: (accountId: Long, goalId: Long) -> Unit,
    viewModel: SavingsGoalsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val screenState by viewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()
    val accountId = screenState.accountId

    var goalToDelete by remember { mutableStateOf<com.resolum.intiva.features.savings.domain.models.SavingGoal?>(null) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
        profileViewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val profile = (profileUiState.profileState as? UiState.Success)?.data
                        val avatarUrl = profile?.avatarUrl?.ifEmpty { null }
                        AsyncImage(
                            model = avatarUrl ?: "https://res.cloudinary.com/dcppsmlzd/image/upload/v1781121388/avatar_default_kf0yvc.png",
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
                    IconButton(onClick = { /* notifications */ }) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = IntivaColors.IconPurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundLavender)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigateToCreate(onNavigateToCreate) },
                containerColor = IntivaColors.PrimaryGreen,
                contentColor = IntivaColors.TextPrimary,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal", modifier = Modifier.size(32.dp))
            }
        },
        containerColor = IntivaColors.BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Mis Metas",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Mantén el control de tus ahorros y alcanza tus objetivos financieros.",
                fontSize = 16.sp,
                color = IntivaColors.TextSecondary,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8E6F1), RoundedCornerShape(12.dp))
                    .padding(6.dp)
            ) {
                TabItem(
                    title = "Personales",
                    isSelected = screenState.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    modifier = Modifier.weight(1f)
                )
                TabItem(
                    title = "Familiares",
                    isSelected = screenState.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val goalsState = screenState.goalsState) {
                is SavingsGoalsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = IntivaColors.PrimaryBrand)
                    }
                }
                is SavingsGoalsUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = goalsState.message,
                            color = IntivaColors.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.refresh() }) {
                            Text("Reintentar", color = IntivaColors.PrimaryBrand)
                        }
                    }
                }
                is SavingsGoalsUiState.Success -> {
                    val filteredGoals = remember(goalsState.goals, screenState.selectedTab) {
                        goalsState.goals.filter { goal ->
                            if (screenState.selectedTab == 0) {
                                goal.ownerType.equals("Individual", ignoreCase = true)
                            } else {
                                goal.ownerType.equals("Group", ignoreCase = true)
                            }
                        }
                    }

                    if (filteredGoals.isEmpty()) {
                        Text(
                            text = if (screenState.selectedTab == 0) {
                                "No tienes metas personales aún."
                            } else {
                                "No hay metas familiares en este grupo."
                            },
                            color = IntivaColors.TextSecondary,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(filteredGoals, key = { it.id }) { goal ->
                                GoalCard(
                                    goal = goal,
                                    onClick = {
                                        accountId?.let { onNavigateToDetail(it, goal.id) }
                                    },
                                    onEditClick = {
                                        accountId?.let { onNavigateToEdit(it, goal.id) }
                                    },
                                    onDeleteClick = { goalToDelete = goal }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    goalToDelete?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            title = { Text("Eliminar meta", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar \"${goal.title}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteGoal(goal.id)
                        goalToDelete = null
                    }
                ) {
                    Text("Eliminar", color = IntivaColors.StatusError, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { goalToDelete = null }) {
                    Text("Cancelar", color = IntivaColors.TextSecondary)
                }
            }
        )
    }
}

@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .height(48.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) IntivaColors.IconPurple else IntivaColors.TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}

@Composable
fun GoalCard(goal: SavingGoal, onClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val progress = goal.progressPercent()
    val savedFormatted = goal.formatAmount(goal.currentAmount)
    val targetFormatted = goal.formatAmount(goal.targetAmount)
    val currencyPrefix = if (goal.currencyCode == "PEN") "S/." else goal.currencyCode

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF2F0FA), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(24.dp).background(IntivaColors.IconPurple, CircleShape))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = goal.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = IntivaColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = IntivaColors.TextSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar meta", color = IntivaColors.TextPrimary, fontSize = 16.sp) },
                            onClick = {
                                expanded = false
                                onEditClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar meta", color = IntivaColors.StatusError, fontSize = 16.sp) },
                            onClick = {
                                expanded = false
                                onDeleteClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ahorrado",
                        fontSize = 14.sp,
                        color = IntivaColors.TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "$currencyPrefix ", fontSize = 18.sp, color = IntivaColors.TextPrimary)
                        Text(
                            text = savedFormatted,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = IntivaColors.TextPrimary
                        )
                    }
                    Text(
                        text = "de $currencyPrefix $targetFormatted",
                        fontSize = 14.sp,
                        color = IntivaColors.TextSecondary
                    )
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(70.dp)) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFF2F0FA),
                        strokeWidth = 7.dp
                    )
                    CircularProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = IntivaColors.PrimaryGreen,
                        strokeWidth = 7.dp,
                        trackColor = Color.Transparent
                    )
                    Text(
                        text = "$progress%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IntivaColors.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = IntivaColors.PrimaryGreen,
                trackColor = Color(0xFFE8E6F1)
            )
        }
    }
}
