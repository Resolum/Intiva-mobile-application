package com.resolum.intiva.features.profiles.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val IntivaPrimary = Color(0xFF534AB7)
private val IntivaBackground = Color(0xFFFAF7FF)
private val IntivaIconBackground = Color(0xFFF1EEFF)
private val IntivaTextPrimary = Color(0xFF1F1B2D)
private val IntivaTextSecondary = Color(0xFF78767E)
private val IntivaDivider = Color(0xFFE9E6F2)
private val IntivaDanger = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ConfiguracionScreen(
    onNavigateToPersonalDetails: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAppearance: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToLinkedAccounts: () -> Unit = {},
    onNavigateToAgreements: () -> Unit = {}
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "INTIVA",
                        color = IntivaTextPrimary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = IntivaPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Configuración",
                color = IntivaTextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(text = "CUENTA")

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Badge,
                    title = "Detalles personales",
                    onClick = onNavigateToPersonalDetails
                )

                SettingsDivider()

                SettingsItem(
                    icon = Icons.Default.Link,
                    title = "Cuentas vinculadas",
                    onClick = onNavigateToLinkedAccounts
                )

                SettingsDivider()

                SettingsItem(
                    icon = Icons.Default.Link,
                    title = "Gestionar categorías",
                    onClick = onNavigateToCategories
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(text = "GENERAL")

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notificaciones",
                    onClick = onNavigateToNotifications
                )

                SettingsDivider()

                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Apariencia",
                    onClick = onNavigateToAppearance
                )

                SettingsDivider()

                SettingsItem(
                    icon = Icons.Default.Badge,
                    title = "Acuerdos",
                    onClick = onNavigateToAgreements
                )
            }

            Spacer(modifier = Modifier.height(28.dp))



            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Versión 1.1.0",
                color = IntivaTextSecondary.copy(alpha = 0.45f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = IntivaTextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 8.dp, bottom = 10.dp)
    )
}

@Composable
private fun SettingsCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(IntivaIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IntivaPrimary,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = IntivaTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color(0xFFC5C1D3),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 70.dp),
        color = IntivaDivider,
        thickness = 1.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacidadSeguridadScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Privacidad y seguridad",
                        fontWeight = FontWeight.Bold,
                        color = IntivaTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = IntivaPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Privacidad y seguridad",
                color = IntivaTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "En Intiva protegemos tu información personal y financiera usando buenas prácticas de seguridad y control de acceso.",
                color = IntivaTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoCard(
                title = "Protección de cuenta",
                description = "Tu sesión ayuda a prevenir accesos no autorizados y protege la información vinculada a tu perfil."
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoCard(
                title = "Uso de datos",
                description = "Procesamos tu información para mostrar métricas, categorías, transacciones y herramientas de control financiero."
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CentroAyudaScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Centro de ayuda",
                        fontWeight = FontWeight.Bold,
                        color = IntivaTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = IntivaPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Centro de ayuda",
                color = IntivaTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Encuentra respuestas rápidas para usar las funciones principales de Intiva.",
                color = IntivaTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            FAQItem(
                question = "¿Cómo registro un nuevo gasto?",
                answer = "Ve a la pantalla principal, selecciona la opción de gasto, completa los datos y guarda la operación."
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = IntivaDivider
            )

            FAQItem(
                question = "¿Qué son los límites de gasto?",
                answer = "Son herramientas para ayudarte a controlar cuánto puedes gastar en un periodo determinado."
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = IntivaDivider
            )

            FAQItem(
                question = "¿Cómo cambio mi foto de perfil?",
                answer = "Ve a Configuración > Detalles personales y selecciona la opción para actualizar tu avatar."
            )
        }
    }
}

@Composable
fun FAQItem(
    question: String,
    answer: String
) {
    Column {
        Text(
            text = question,
            color = IntivaTextPrimary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = answer,
            color = IntivaTextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    onNavigateBack: () -> Unit
) {
    PlaceholderScreen(
        title = "Notificaciones",
        message = "Pantalla de configuración de notificaciones próximamente.",
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AparienciaScreen(
    onNavigateBack: () -> Unit
) {
    PlaceholderScreen(
        title = "Apariencia",
        message = "Pantalla de configuración de apariencia y temas próximamente.",
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(
    title: String,
    message: String,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = IntivaTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = IntivaPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = IntivaTextSecondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                color = IntivaTextPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = description,
                color = IntivaTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}