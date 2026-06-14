package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.savings.domain.models.SavingGoalStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalEditScreen(
    goalId: String,
    onNavigateBack: () -> Unit,
    onGoalUpdated: () -> Unit,
    onGoalCompleted: ((String) -> Unit)? = null,
    viewModel: SavingsGoalEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(goalId) {
        goalId.toLongOrNull()?.let { viewModel.loadGoal(it) }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.onSaveHandled()
            onGoalUpdated()
        }
    }

    LaunchedEffect(uiState.goalJustCompleted) {
        if (uiState.goalJustCompleted) {
            viewModel.onGoalCompletedHandled()
            onGoalCompleted?.invoke(goalId)
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMessageHandled()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMessageHandled()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Modificar Meta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.PrimaryBrand)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IntivaColors.BackgroundSurface)
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { viewModel.save() },
                    enabled = !uiState.isLoading && !uiState.isStatusLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryAction),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = IntivaColors.TextPrimary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = IntivaColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "GUARDAR CAMBIOS",
                            color = IntivaColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        containerColor = IntivaColors.BackgroundSurface
    ) { padding ->

        if (uiState.isLoading && uiState.title.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = IntivaColors.PrimaryBrand)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "MONTO OBJETIVO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "S/ ", fontSize = 28.sp, color = IntivaColors.TextSecondary, modifier = Modifier.padding(bottom = 6.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            BasicTextField(
                                value = uiState.targetAmountInput,
                                onValueChange = { viewModel.updateTargetAmount(it) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IntivaColors.PrimaryBrand
                                ),
                                singleLine = true
                            )
                            Box(modifier = Modifier.width(100.dp).height(4.dp).background(IntivaColors.PrimaryAction, RoundedCornerShape(2.dp)))
                            if (uiState.targetAmountError != null) {
                                Text(uiState.targetAmountError!!, color = IntivaColors.StatusError, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(72.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(IntivaColors.PrimaryBrand, CircleShape)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎯", color = Color.White, fontSize = 32.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.White, CircleShape)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar ícono", tint = IntivaColors.TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(text = "Nombre de la meta", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        BasicTextField(
                            value = uiState.title,
                            onValueChange = { viewModel.updateTitle(it) },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp, 
                                color = IntivaColors.TextPrimary, 
                                fontWeight = FontWeight.Bold
                            ),
                            singleLine = true
                        )
                        if (uiState.titleError != null) {
                            Text(uiState.titleError!!, color = IntivaColors.StatusError, fontSize = 12.sp)
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Descripción", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = uiState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            color = IntivaColors.TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        decorationBox = { innerTextField ->
                            if (uiState.description.isEmpty()) {
                                Text("Opcional", color = IntivaColors.TextSecondary, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Fecha límite", fontSize = 14.sp, color = IntivaColors.TextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = uiState.deadline.ifEmpty { "Sin fecha" }, fontSize = 18.sp, color = IntivaColors.TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF2F0FA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IntivaColors.PrimaryBrand, modifier = Modifier.size(24.dp))
                    }
                }
            }
            

            Text(
                text = "Estado de la meta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary,
                modifier = Modifier.padding(top = 16.dp)
            )

            StatusOption(
                label = "En progreso",
                description = "La meta está activa y acepta contribuciones.",
                isSelected = uiState.currentStatus == SavingGoalStatus.INPROGRESS,
                isLoading = uiState.isStatusLoading,
                statusColor = IntivaColors.PrimaryBrand,
                onClick = null
            )

            StatusOption(
                label = "Completada",
                description = "Marca la meta como alcanzada exitosamente.",
                isSelected = uiState.currentStatus == SavingGoalStatus.COMPLETED,
                isLoading = uiState.isStatusLoading,
                statusColor = Color(0xFF4CAF50),
                onClick = {
                    if (uiState.currentStatus != SavingGoalStatus.COMPLETED) {
                        viewModel.markAsCompleted()
                    }
                }
            )

            StatusOption(
                label = "No completada",
                description = "Revierte el estado a no completada.",
                isSelected = uiState.currentStatus == SavingGoalStatus.UNCOMPLETED,
                isLoading = uiState.isStatusLoading,
                statusColor = IntivaColors.StatusError,
                onClick = {
                    if (uiState.currentStatus != SavingGoalStatus.UNCOMPLETED) {
                        viewModel.markAsUncompleted()
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StatusOption(
    label: String,
    description: String,
    isSelected: Boolean,
    isLoading: Boolean,
    statusColor: Color,
    onClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(enabled = !isLoading) { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) statusColor.copy(alpha = 0.08f) else Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, statusColor)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading && onClick != null) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = statusColor,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isSelected) statusColor else Color(0xFFBDBDBD),
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) statusColor else IntivaColors.TextPrimary
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = IntivaColors.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
