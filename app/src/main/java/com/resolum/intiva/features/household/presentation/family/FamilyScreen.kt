package com.resolum.intiva.features.household.presentation.family

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.theme.IntivaColors
import com.resolum.intiva.features.household.domain.models.FamilyRole
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.features.household.presentation.family.components.FamilyActivityItem
import com.resolum.intiva.features.household.presentation.family.components.FamilyHeaderCard
import com.resolum.intiva.features.household.presentation.family.components.InviteMemberButton
import com.resolum.intiva.features.household.presentation.family.components.MemberListItem
import com.resolum.intiva.features.household.presentation.family.components.QrCodeScannerContent
import com.resolum.intiva.features.household.presentation.invitation.InviteBottomSheet
import com.resolum.intiva.features.household.presentation.invite.InviteViewModel
import com.resolum.intiva.features.household.presentation.invite.components.QrCodeImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    viewModel: FamilyViewModel = hiltViewModel(),
    onInviteClick: () -> Unit = {},
    onViewAllActivity: () -> Unit = {},
    onContributeClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val inviteViewModel: InviteViewModel = hiltViewModel()

    var showQrOptionsSheet by remember { mutableStateOf(false) }
    var showQrScanner by remember { mutableStateOf(false) }
    var showMyQrSheet by remember { mutableStateOf(false) }
    var showCameraDeniedDialog by remember { mutableStateOf(false) }
    var qrScanConsumed by remember { mutableStateOf(false) }
    var scannedInviteData by remember { mutableStateOf<DeepLinkData?>(null) }

    val isAdmin = (uiState.membersState as? UiState.Success)?.data
        ?.any { it.role == FamilyRole.ADMIN.name } == true

    val cameraPermissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            showQrScanner = true
        } else {
            showCameraDeniedDialog = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadFamily()
        viewModel.loadMembers()
    }

    LaunchedEffect(uiState.scanResultState) {
        when (val state = uiState.scanResultState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar(state.data)
                qrScanConsumed = true
                delay(200)
                showQrScanner = false
                showQrOptionsSheet = false
                viewModel.resetScanResult()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                qrScanConsumed = true
                delay(200)
                viewModel.resetScanResult()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            val familyName = (uiState.familyState as? UiState.Success)?.data?.name ?: "Familia"
            TopAppBar(
                title = {
                    Text(
                        text = familyName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = IntivaColors.TextPrimary
                    )
                },
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = { showQrOptionsSheet = true }) {
                            Icon(
                                imageVector = Icons.Outlined.QrCodeScanner,
                                contentDescription = "Escanear QR",
                                tint = IntivaColors.PrimaryBrand
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
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
                                totalBalance = uiState.totalBalance,
                                onContributeClick = onContributeClick
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

    if (showQrOptionsSheet) {
        QrOptionsBottomSheet(
            onDismiss = { showQrOptionsSheet = false },
            onScanQr = {
                showQrOptionsSheet = false
                if (cameraPermissionGranted) {
                    showQrScanner = true
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onShowMyQr = {
                showQrOptionsSheet = false
                viewModel.loadFamilyQrCode()
                showMyQrSheet = true
            }
        )
    }

    if (showQrScanner) {
        ModalBottomSheet(
            onDismissRequest = {
                showQrScanner = false
                qrScanConsumed = false
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                QrCodeScannerContent(
                    onQrCodeDetected = { rawValue ->
                        if (!qrScanConsumed) {
                            qrScanConsumed = true
                            val parsed = parseQrContent(rawValue)
                            if (parsed != null) {
                                scannedInviteData = parsed
                            } else {
                                CoroutineScope(Dispatchers.Main).launch {
                                    snackbarHostState.showSnackbar("QR no reconocido")
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cancelar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clickable {
                            showQrScanner = false
                            qrScanConsumed = false
                        }
                )
            }
        }
    }

    if (showMyQrSheet) {
        MyQrBottomSheet(
            uiState = uiState,
            onDismiss = { showMyQrSheet = false },
            onRetry = { viewModel.loadFamilyQrCode() }
        )
    }

    if (showCameraDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showCameraDeniedDialog = false },
            title = {
                Text(
                    text = "Permiso de cámara requerido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Para escanear códigos QR necesitamos acceso a tu cámara. Puedes habilitarlo desde Configuración.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showCameraDeniedDialog = false }) {
                    Text(
                        text = "Entendido",
                        color = IntivaColors.PrimaryBrand,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = Color.White
        )
    }

    scannedInviteData?.let { inviteData ->
        InviteBottomSheet(
            token = inviteData.token,
            viewModel = inviteViewModel,
            groupName = inviteData.groupName,
            inviterName = inviteData.inviterName,
            onDismiss = { scannedInviteData = null },
            onAccepted = {
                scannedInviteData = null
                showQrScanner = false
                viewModel.loadFamily()
                viewModel.loadMembers()
            },
            onDeclined = {
                scannedInviteData = null
                showQrScanner = false
            }
        )
    }
}

private fun parseQrContent(rawValue: String): DeepLinkData? {
    val uri = Uri.parse(rawValue)
    if (uri.scheme == "miapp" && uri.host == "invite") {
        val token = uri.getQueryParameter("token") ?: return null
        val group = uri.getQueryParameter("group") ?: ""
        val inviter = uri.getQueryParameter("inviter") ?: ""
        return DeepLinkData(token = token, groupName = group, inviterName = inviter)
    }
    val host = uri.host
    if (host != null && (host.contains("intiva"))) {
        val token = uri.getQueryParameter("token") ?: return null
        return DeepLinkData(token = token)
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QrOptionsBottomSheet(
    onDismiss: () -> Unit,
    onScanQr: () -> Unit,
    onShowMyQr: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "¿Qué deseas hacer?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(24.dp))

            QrOptionRow(
                icon = Icons.Outlined.QrCodeScanner,
                label = "Escanear QR de invitación",
                onClick = onScanQr
            )

            HorizontalDivider(color = Color(0xFFE5E7EB))

            QrOptionRow(
                icon = Icons.Outlined.QrCode,
                label = "Mostrar mi QR",
                onClick = onShowMyQr
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun QrOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = IntivaColors.PrimaryBrand,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF0D0D0D),
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyQrBottomSheet(
    uiState: FamilyUiState,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Código QR del grupo",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D0D0D)
            )

            Spacer(Modifier.height(24.dp))

            when (val qrState = uiState.qrCodeState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(color = IntivaColors.PrimaryBrand)
                }

                is UiState.Success -> {
                    QrCodeImage(
                        qrBase64 = qrState.data.qrBase64,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Pide a tus familiares que escaneen este código",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val link = qrState.data.invitationLink
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Únete a mi grupo familiar en Intiva: $link")
                            }
                            context.startActivity(Intent.createChooser(intent, "Compartir invitación"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFCCFF00),
                            contentColor = Color(0xFF0D0D0D)
                        )
                    ) {
                        Text(
                            text = "COMPARTIR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = qrState.message,
                        fontSize = 14.sp,
                        color = IntivaColors.StatusError,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onRetry,
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IntivaColors.PrimaryBrand)
                    ) {
                        Text("Reintentar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                else -> {
                    onRetry()
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Cerrar",
                color = IntivaColors.PrimaryBrand,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDismiss() }
            )
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
