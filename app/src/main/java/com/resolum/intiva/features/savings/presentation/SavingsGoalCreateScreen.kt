package com.resolum.intiva.features.savings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val BrandPurple = Color(0xFF534AB7)
private val AccentGreen = Color(0xFFCDEB45)
private val NeutralGray = Color(0xFF78767E)

/**
 * Screen used to create a new savings goal with fields bound to [SavingsGoalCreateViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalCreateScreen(
    accountId: Long,
    onNavigateBack: () -> Unit,
    onGoalCreated: (accountId: Long, goalId: Long) -> Unit,
    viewModel: SavingsGoalCreateViewModel = hiltViewModel()
) {
    val formState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showGroupMenu by remember { mutableStateOf(false) }

    LaunchedEffect(accountId) {
        viewModel.initialize(accountId)
    }

    LaunchedEffect(formState.createdGoal) {
        formState.createdGoal?.let { goal ->
            onGoalCreated(accountId, goal.id)
            viewModel.onGoalCreationHandled()
        }
    }

    val formattedAmount = viewModel.formattedTargetAmountPreview()
    val deadlineLabel = formState.deadline?.format(
        DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale("es", "ES"))
    ) ?: "Seleccionar fecha"

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = formState.deadline
                ?.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()
                ?.toEpochMilli()
                ?: System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return date.isAfter(LocalDate.now())
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateDeadline(date)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = BrandPurple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = NeutralGray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Nueva Meta",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 48.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = IntivaColors.TextPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundLavender)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IntivaColors.BackgroundLavender)
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { viewModel.submitGoal() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen,
                        disabledContainerColor = Color(0xFFE8E6F1)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = formState.isFormValid && !formState.isLoading
                ) {
                    if (formState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = IntivaColors.TextPrimary,
                            strokeWidth = 2.dp
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
                            text = "Guardar Meta",
                            color = IntivaColors.TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                formState.submitError?.let { error ->
                    Text(
                        text = error,
                        color = IntivaColors.ErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        containerColor = IntivaColors.BackgroundLavender
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Target amount
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MONTO OBJETIVO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "S/",
                            fontSize = 28.sp,
                            color = NeutralGray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = formState.targetAmountInput,
                            onValueChange = viewModel::updateTargetAmount,
                            modifier = Modifier.widthIn(min = 120.dp, max = 200.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandPurple
                            ),
                            placeholder = {
                                Text("0", fontSize = 36.sp, color = BrandPurple.copy(alpha = 0.4f))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = Color.Transparent,
                                errorBorderColor = IntivaColors.ErrorRed
                            ),
                            isError = formState.targetAmountError != null
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vista previa: S/ $formattedAmount",
                        fontSize = 14.sp,
                        color = NeutralGray
                    )
                    formState.targetAmountError?.let {
                        FieldErrorText(it)
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .width(80.dp)
                            .height(4.dp)
                            .background(AccentGreen, RoundedCornerShape(2.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
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
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(BrandPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(32.dp).background(Color.White, CircleShape))
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = NeutralGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nombre de la meta",
                            fontSize = 14.sp,
                            color = NeutralGray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = formState.title,
                            onValueChange = viewModel::updateTitle,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej. Viaje a Cusco") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = Color(0xFFE8E6F1),
                                errorBorderColor = IntivaColors.ErrorRed
                            ),
                            isError = formState.titleError != null
                        )
                        formState.titleError?.let { FieldErrorText(it) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Deadline
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Fecha límite",
                        fontSize = 14.sp,
                        color = NeutralGray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = deadlineLabel,
                            fontSize = 18.sp,
                            color = if (formState.deadline != null) IntivaColors.TextPrimary else NeutralGray,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = BrandPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    formState.deadlineError?.let { FieldErrorText(it) }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Goal type
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Tipo de meta",
                        fontSize = 14.sp,
                        color = NeutralGray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF2F0FA), RoundedCornerShape(12.dp))
                            .padding(6.dp)
                    ) {
                        TypeTab(
                            title = "Personal",
                            icon = Icons.Default.Person,
                            isSelected = !formState.isFamilyGoal,
                            onClick = { viewModel.setPersonalGoal() },
                            modifier = Modifier.weight(1f)
                        )
                        TypeTab(
                            title = "Familiar",
                            icon = Icons.Default.Group,
                            isSelected = formState.isFamilyGoal,
                            onClick = { viewModel.setFamilyGoal() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (formState.isFamilyGoal) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Seleccionar Grupo",
                            fontSize = 14.sp,
                            color = NeutralGray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { showGroupMenu = true }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(AccentGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Group,
                                        contentDescription = null,
                                        tint = IntivaColors.TextPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        formState.selectedGroupLabel,
                                        fontSize = 18.sp,
                                        color = IntivaColors.TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = NeutralGray,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showGroupMenu,
                                onDismissRequest = { showGroupMenu = false }
                            ) {
                                formState.availableGroups.forEach { group ->
                                    DropdownMenuItem(
                                        text = { Text(group.label) },
                                        onClick = {
                                            viewModel.selectGroup(group.groupId)
                                            showGroupMenu = false
                                        }
                                    )
                                }
                            }
                        }
                        formState.groupError?.let { FieldErrorText(it) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun FieldErrorText(message: String) {
    Text(
        text = message,
        color = IntivaColors.ErrorRed,
        fontSize = 13.sp,
        modifier = Modifier.padding(top = 4.dp)
    )
}

/**
 * Tab item used in the goal type selector (Personal vs Family).
 */
@Composable
fun TypeTab(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .height(48.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) BrandPurple else NeutralGray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = if (isSelected) BrandPurple else NeutralGray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
