package com.resolum.intiva.features.household.presentation.invitation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.household.presentation.invite.InviteViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteBottomSheet(
    token: String,
    viewModel: InviteViewModel,
    groupName: String = "",
    inviterName: String = "",
    onDismiss: () -> Unit,
    onAccepted: () -> Unit,
    onDeclined: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    var showRejectDialog by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            viewModel.loadInvitationByToken(token)
        }
    }

    LaunchedEffect(uiState.actionState) {
        when (val state = uiState.actionState) {
            is UiState.Success -> {
                val message = state.data
                snackbarHostState.showSnackbar(message)
                delay(1500)
                if (message.contains("rechazada", ignoreCase = true)) {
                    onDeclined()
                } else {
                    onAccepted()
                }
            }
            is UiState.Error -> {
                val errorMsg = if (state.message.contains("rechazar", ignoreCase = true) ||
                    state.message.contains("rechaz", ignoreCase = true)
                ) {
                    "No se pudo rechazar, intenta de nuevo"
                } else {
                    state.message
                }
                snackbarHostState.showSnackbar(errorMsg)
            }
            else -> {}
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val state = uiState.invitationDetail) {
                    is UiState.Loading -> {
                        Spacer(Modifier.height(48.dp))
                        CircularProgressIndicator(color = Color(0xFF4C3FF7))
                        Spacer(Modifier.height(48.dp))
                    }

                    is UiState.Success -> {
                        val invitation = state.data
                        val isLoading = uiState.actionState is UiState.Loading

                        Spacer(Modifier.height(24.dp))

                        Text(
                            text = "Invitación Familiar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF0D0D0D),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "${invitation.invitedByName} te ha invitado a unirte a su grupo de gestión financiera compartida.",
                            fontSize = 15.sp,
                            color = Color(0xFF0D0D0D),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.acceptInvite(token) },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA8D84A),
                                contentColor = Color(0xFF0D0D0D)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color(0xFF0D0D0D),
                                    modifier = Modifier.height(18.dp)
                                )
                            } else {
                                Text(
                                    text = "UNIRME AL GRUPO",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = "RECHAZAR",
                            color = if (isLoading) Color(0xFFD1D5DB) else Color(0xFF6B7280),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(enabled = !isLoading) {
                                    showRejectDialog = true
                                }
                        )

                        Spacer(Modifier.height(8.dp))
                    }

                    is UiState.Error -> {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = state.message,
                            fontSize = 15.sp,
                            color = Color(0xFFEF4444),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "CERRAR",
                            color = Color(0xFF4C3FF7),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onDismiss() }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    is UiState.Idle -> {
                        Spacer(Modifier.height(48.dp))
                        CircularProgressIndicator(color = Color(0xFF4C3FF7))
                        Spacer(Modifier.height(48.dp))
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }

    if (showRejectDialog) {
        val displayInviter = inviterName.ifBlank {
            (uiState.invitationDetail as? UiState.Success)?.data?.invitedByName ?: ""
        }
        val displayGroup = groupName.ifBlank { "su grupo familiar" }

        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = {
                Text(
                    text = "¿Rechazar invitación?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "$displayInviter recibirá una notificación de que rechazaste unirte a $displayGroup.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRejectDialog = false
                        viewModel.rejectInvite(token)
                    }
                ) {
                    Text(
                        text = "Sí, rechazar",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text(
                        text = "Cancelar",
                        color = Color(0xFF6B7280)
                    )
                }
            },
            containerColor = Color.White
        )
    }
}
