package com.resolum.intiva.features.household.presentation.invitation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.features.household.presentation.invitation.components.AcceptInvitationButton
import com.resolum.intiva.features.household.presentation.invitation.components.BenefitsCard

private val BrandPurple = Color(0xFF4C3FF7)
private const val LANDING_URL = "https://intiva.vercel.app/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationDetailScreen(
    viewModel: InvitationDetailViewModel = hiltViewModel(),
    onAccepted: () -> Unit = {},
    onDeclined: () -> Unit = {},
    onGoToLanding: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(actionState) {
        when (actionState) {
            is InvitationDetailViewModel.InvitationActionState.Success -> {
                val msg = (actionState as InvitationDetailViewModel.InvitationActionState.Success).message
                if (msg.contains("aceptada", ignoreCase = true)) onAccepted()
                else if (msg.contains("rechazada", ignoreCase = true)) onDeclined()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandPurple)
            )
        }
    ) { innerPadding ->

        when (val state = uiState) {
            is InvitationDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrandPurple)
                }
            }
            is InvitationDetailUiState.Success -> {
                val invitation = state.invitation
                val isLoading =
                    actionState is InvitationDetailViewModel.InvitationActionState.Loading

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = "Invitación Familiar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color(0xFF0D0D0D),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(invitation.invitedByName)
                            }
                            append(" te ha invitado a unirte a su grupo de gestión financiera compartida.")
                        },
                        fontSize = 16.sp,
                        color = Color(0xFF0D0D0D),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "BENEFICIOS DEL GRUPO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    BenefitsCard(modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(32.dp))

                    if (state.isUserLoggedIn) {
                        AcceptInvitationButton(
                            onClick = { viewModel.acceptInvitation(invitation.id) }
                        )

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = "DECLINAR",
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(enabled = !isLoading) {
                                    viewModel.rejectInvitation(invitation.id)
                                }
                        )
                    } else {
                        AcceptInvitationButton(onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(LANDING_URL))
                            )
                        })
                    }

                    Spacer(Modifier.height(40.dp))

                    IntivaLandingLink(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LANDING_URL)))
                    })

                    Spacer(Modifier.height(32.dp))
                }
            }

            is InvitationDetailUiState.Expired -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text("Invitación expirada", fontWeight = FontWeight.Bold,
                        fontSize = 20.sp, color = Color(0xFF0D0D0D))
                    Spacer(Modifier.height(8.dp))
                    Text(state.message, fontSize = 14.sp,
                        color = Color(0xFF6B7280), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(32.dp))
                    IntivaLandingLink(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LANDING_URL)))
                    })
                }
            }

            is InvitationDetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text(state.message, fontSize = 15.sp,
                        color = Color(0xFFEF4444), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))
                    IntivaLandingLink(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LANDING_URL)))
                    })
                }
            }
        }
    }
}

@Composable
private fun IntivaLandingLink(onClick: () -> Unit) {
    HorizontalDivider(color = Color(0xFFE5E7EB))
    Spacer(Modifier.height(16.dp))
    Text(
        text = buildAnnotatedString {
            append("¿No tienes cuenta? ")
            withStyle(
                SpanStyle(
                    color = BrandPurple,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Conoce Intiva aquí →")
            }
        },
        fontSize = 14.sp,
        color = Color(0xFF6B7280),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    )
}
