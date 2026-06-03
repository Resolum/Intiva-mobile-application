package com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.paymentmethodsandcategories.presentation.financialaccount.components.FinancialAccountCard

private val IntivaPrimary = Color(0xFF534AB7)
private val IntivaSecondary = Color(0xFFCDEB45)
private val IntivaNeutral = Color(0xFF78767E)
private val IntivaBackground = Color(0xFFF4F0FA)
private val IntivaCardBackground = Color(0xFFF8F5FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialAccountScreen(
    viewModel: FinancialAccountViewModel = hiltViewModel(),
    onAddAccountClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFinancialAccounts()
    }

    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cuentas financieras",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IntivaPrimary
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            FilledTonalButton(
                onClick = onAddAccountClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = IntivaSecondary,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null
                )

                Text(
                    text = "Agregar tarjeta o billetera",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState.accountsState) {
                is UiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = IntivaPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Cargando cuentas...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = IntivaNeutral
                        )
                    }
                }

                is UiState.Success -> {
                    if (uiState.accounts.isEmpty()) {
                        EmptyFinancialAccountsContent(
                            onAddAccountClick = onAddAccountClick
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(uiState.accounts) { account ->
                                FinancialAccountCard(account = account)
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    EmptyFinancialAccountsContent(
                        onAddAccountClick = onAddAccountClick
                    )
                }

                UiState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun EmptyFinancialAccountsContent(
    onAddAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = IntivaCardBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = IntivaPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CreditCard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Aún no tienes cuentas registradas",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agrega una tarjeta de débito, tarjeta de crédito o billetera digital.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IntivaNeutral
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onAddAccountClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IntivaPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Agregar cuenta")
                }
            }
        }
    }
}