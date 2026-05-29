package com.resolum.intiva.features.finances.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.snackbar.IntivaSnackBarHost
import com.resolum.intiva.core.ui.snackbar.SnackBarType
import com.resolum.intiva.core.ui.snackbar.SnackBarVisualsWithType
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.finances.presentation.transactions.TransactionViewModel
import com.resolum.intiva.features.finances.presentation.transactions.components.TransactionItem

/**
 * Main dashboard screen representing the user's financial overview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNewExpense: () -> Unit,
    onNavigateToNewIncome: () -> Unit,
    navController: NavController,
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateToTransactions: () -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.getTransactionsByOwnerId(onlyLatest = true)

        val success = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<Boolean>("transaction_success")

        if (success == true) {
            snackBarHostState.showSnackbar(
                SnackBarVisualsWithType(
                    message = "Transacción registrada exitosamente",
                    type = SnackBarType.Success
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Intiva", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = IntivaColors.TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = IntivaColors.IconPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IntivaColors.BackgroundLavender)
            )
        },
        containerColor = IntivaColors.BackgroundLavender,
        snackbarHost = {
            IntivaSnackBarHost(hostState = snackBarHostState)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            item {
                Column {
                    Text(
                        text = "Hola, Jennifer 👋",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aquí está el resumen de tus finanzas hoy.",
                        fontSize = 14.sp,
                        color = IntivaColors.TextSecondary
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = IntivaColors.BackgroundPurple)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "BALANCE TOTAL", fontSize = 12.sp, color = Color(0xCCFFFFFF), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(text = "S/ ", fontSize = 20.sp, color = Color.White)
                            Text(text = "12,400.00", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(20.dp))


                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = {
                                    onNavigateToNewExpense()
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF0D0D0D), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gasto", color = Color(0xFF0D0D0D), fontWeight = FontWeight.Medium)
                            }

                            Button(
                                onClick = {
                                    onNavigateToNewIncome()
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B5FD4)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ingreso", color = Color.White, fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(text = "Ver cuentas >", color = Color(0xCCFFFFFF), fontSize = 12.sp)
                        }
                    }
                }
            }


            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.fillMaxSize(),
                                color = Color(0xFFF2F0FA),
                                strokeWidth = 5.dp
                            )
                            CircularProgressIndicator(
                                progress = { 0.65f },
                                modifier = Modifier.fillMaxSize(),
                                color = IntivaColors.PrimaryGreen,
                                strokeWidth = 5.dp,
                                trackColor = Color.Transparent
                            )
                            Text(
                                text = "65%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = IntivaColors.TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Límite de Gasto Mensual", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = IntivaColors.TextPrimary)
                            Text(text = "Te quedan S/ 1,200.00 disponibles", fontSize = 12.sp, color = IntivaColors.TextSecondary)
                        }
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Actividad Reciente", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = IntivaColors.TextPrimary)
                    Text(text = "Ver todo", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = IntivaColors.IconPurple, modifier = Modifier.clickable { onNavigateToTransactions() })
                }
            }

            item {
                when (val state = uiState.transactionsState) {

                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = IntivaColors.IconPurple)
                        }
                    }

                    is UiState.Success -> {
                        val allTransactions = state.data
                            .flatMap { it.transactions }

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            allTransactions.forEach { transaction ->
                                TransactionItem(transaction = transaction)
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = "Error al cargar transacciones",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}
