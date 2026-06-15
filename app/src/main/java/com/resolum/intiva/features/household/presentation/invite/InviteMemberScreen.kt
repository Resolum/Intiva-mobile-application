package com.resolum.intiva.features.household.presentation.invite

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.household.presentation.invite.components.QrCodeImage
import com.resolum.intiva.features.household.presentation.invite.components.ShareInvitationButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteMemberScreen(
    viewModel: InviteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Converts the backend HTTPS link to the custom scheme so Android opens the app
    fun toAppLink(httpsLink: String): String {
        return httpsLink
            .replace("https://intiva.app/lander", "intiva://lander")
            .replace("https://intiva.app/join", "intiva://join")
            .replace("https://intiva-1406c.web.app/lander", "intiva://lander")
            .replace("https://intiva-1406c.web.app/join", "intiva://join")
            .replace("https://intiva-1406c.firebaseapp.com/lander", "intiva://lander")
            .replace("https://intiva-1406c.firebaseapp.com/join", "intiva://join")
    }

    LaunchedEffect(Unit) {
        viewModel.loadQrCode()
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F7),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Invitar Miembro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Comparte el código QR o enlace de invitación",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4C3FF7))
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Enlace de invitación",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF374151),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Genera un enlace para compartir con quien quieras invitar a tu grupo familiar.",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val linkState = uiState.linkInviteState) {
                is UiState.Success -> {
                    val inviteUrl = linkState.data

                    Text(
                        text = inviteUrl,
                        fontSize = 12.sp,
                        color = Color(0xFF4C3FF7),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ShareInvitationButton(
                        text = "COMPARTIR ENLACE",
                        onClick = {
                            val link = toAppLink(inviteUrl)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Únete a mi grupo familiar en Intiva: $link")
                            }
                            context.startActivity(Intent.createChooser(intent, "Compartir enlace"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color(0xFFCCFF00),
                        contentColor = Color(0xFF0D0D0D)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            val link = toAppLink(inviteUrl)
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                                    as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Invitación Intiva", link))
                            scope.launch { snackbarHostState.showSnackbar("Enlace copiado") }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "COPIAR ENLACE", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF4C3FF7))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { viewModel.clearLinkInviteState() },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Generar nuevo enlace", fontWeight = FontWeight.Medium, color = Color(0xFF6B7280))
                    }
                }

                is UiState.Loading -> {
                    CircularProgressIndicator(color = Color(0xFF4C3FF7))
                }

                is UiState.Error -> {
                    Text(
                        text = linkState.message,
                        fontSize = 13.sp,
                        color = Color(0xFFEF4444),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.generateLinkInvitation() },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C3FF7))
                    ) {
                        Text(text = "Reintentar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                else -> {
                    Button(
                        onClick = { viewModel.generateLinkInvitation() },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C3FF7))
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Link,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Generar enlace", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFD1D5DB)
                )
                Text(
                    text = "  o comparte el código QR  ",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFD1D5DB)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.noActiveInvitation -> {
                    Icon(
                        imageVector = Icons.Outlined.QrCode,
                        contentDescription = null,
                        tint = Color(0xFF4C3FF7).copy(alpha = 0.25f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Sin invitación activa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF374151),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Este hogar no tiene un código QR activo aún. Genera uno para que otros puedan unirse.",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center
                    )
                    val createError = uiState.createError
                    if (createError != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = createError,
                            fontSize = 12.sp,
                            color = Color(0xFFEF4444),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = { viewModel.generateQrInvitation() },
                        enabled = !uiState.isCreatingInvitation,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C3FF7))
                    ) {
                        if (uiState.isCreatingInvitation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Generando...", color = Color.White, fontWeight = FontWeight.Bold)
                        } else {
                            Text(text = "Generar Código QR", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { viewModel.loadQrCode() },
                        enabled = !uiState.isCreatingInvitation,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Verificar de nuevo", fontWeight = FontWeight.Medium, color = Color(0xFF4C3FF7))
                    }
                }

                else -> when (val qrState = uiState.qrState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(color = Color(0xFF4C3FF7))
                    }

                    is UiState.Success -> {
                        QrCodeImage(
                            qrBase64 = qrState.data.qrBase64,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ShareInvitationButton(
                            text = "COMPARTIR QR",
                            onClick = {
                                val link = toAppLink(viewModel.getInvitationLink())
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Únete a mi grupo familiar en Intiva: $link")
                                }
                                context.startActivity(Intent.createChooser(intent, "Compartir invitación"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color(0xFFCCFF00),
                            contentColor = Color(0xFF0D0D0D)
                        )

                    }

                    is UiState.Error -> {
                        Icon(
                            imageVector = Icons.Outlined.QrCode,
                            contentDescription = null,
                            tint = Color(0xFFD1D5DB),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No se pudo cargar el código QR",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF374151),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = qrState.message,
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.loadQrCode() },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C3FF7))
                        ) {
                            Text(text = "Reintentar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
