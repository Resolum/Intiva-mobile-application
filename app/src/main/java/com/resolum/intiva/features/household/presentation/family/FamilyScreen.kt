package com.resolum.intiva.features.household.presentation.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.household.presentation.family.components.FamilyActivityItem
import com.resolum.intiva.features.household.presentation.family.components.FamilyHeaderCard
import com.resolum.intiva.features.household.presentation.family.components.InviteMemberButton
import com.resolum.intiva.features.household.presentation.family.components.MemberListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    viewModel: FamilyViewModel = hiltViewModel(),
    onInviteClick: () -> Unit = {},
    onViewAllActivity: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFamily()
        viewModel.loadMembers()
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val familyState = uiState.familyState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = IntivaColors.PrimaryBrand)
                    }
                }

                is UiState.Idle -> {
                    NoFamilyContent(
                        onCreateFamily = { name, description ->
                            viewModel.createFamily(name, description)
                        }
                    )
                }

                is UiState.Success -> {
                    val family = familyState.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            FamilyHeaderCard(
                                groupName = family.name,
                                totalBalance = "S/ 0.00",
                                onContributeClick = {}
                            )
                        }

                        item {
                            Text(
                                text = "Miembros",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = IntivaColors.TextPrimary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        when (val membersState = uiState.membersState) {
                            is UiState.Success -> {
                                items(membersState.data) { member ->
                                    MemberListItem(
                                        memberName = "Miembro #${member.userId}",
                                        memberRole = if (member.role == "ADMIN") "Admin" else "Miembro",
                                        onMenuClick = {}
                                    )
                                }
                            }
                            is UiState.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        color = IntivaColors.PrimaryBrand
                                    )
                                }
                            }
                            else -> {}
                        }

                        item {
                            InviteMemberButton(onClick = onInviteClick)
                        }

                        item {
                            Text(
                                text = "Actividad Familiar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = IntivaColors.TextPrimary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        item {
                            FamilyActivityItem(onViewAll = onViewAllActivity)
                        }
                    }
                }

                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = familyState.message,
                                color = IntivaColors.StatusError,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                viewModel.loadFamily()
                                viewModel.loadMembers()
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoFamilyContent(
    onCreateFamily: (name: String, description: String) -> Unit
) {
    var familyName by remember { mutableStateOf("") }
    var familyDesc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(IntivaColors.PrimaryBrand.copy(alpha = 0.1f), shape = RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = IntivaColors.PrimaryBrand,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Aún no tienes un grupo familiar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = IntivaColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Crea un grupo para gestionar finanzas\nen familia y hacer seguimiento conjunto.",
            fontSize = 14.sp,
            color = IntivaColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = familyName,
            onValueChange = { familyName = it },
            label = { Text("Nombre del grupo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = familyDesc,
            onValueChange = { familyDesc = it },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (familyName.isNotBlank()) {
                    onCreateFamily(familyName.trim(), familyDesc.trim())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryBrand),
            enabled = familyName.isNotBlank()
        ) {
            Text(
                text = "Crear grupo familiar",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
