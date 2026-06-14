package com.resolum.intiva.features.iam.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val IntivaPrimary = Color(0xFF534AB7)
private val IntivaBackground = Color(0xFFFAF7FF)
private val IntivaSurface = Color.White
private val IntivaTextPrimary = Color(0xFF1F1B2D)
private val IntivaTextSecondary = Color(0xFF78767E)
private val IntivaDivider = Color(0xFFE9E6F2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "Política de Privacidad",
                        color = IntivaTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
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
                    containerColor = IntivaSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntivaBackground)
                .padding(paddingValues)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Política de Privacidad de Intiva",
                color = IntivaTextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Última actualización: junio de 2026",
                color = IntivaTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            PrivacySummaryCard()

            Spacer(modifier = Modifier.height(20.dp))

            PrivacySection(
                title = "1. Información que recopilamos",
                body = "Intiva puede recopilar información necesaria para crear y administrar tu cuenta, como nombre, correo electrónico, foto de perfil y datos de identificación interna del usuario."
            )

            PrivacySection(
                title = "2. Información financiera registrada",
                body = "La aplicación permite registrar datos financieros como ingresos, gastos, categorías, métodos de pago, cuentas financieras, metas de ahorro y movimientos económicos. Esta información es utilizada para mostrar reportes, resúmenes y funcionalidades de organización financiera."
            )

            PrivacySection(
                title = "3. Uso de la información",
                body = "Utilizamos la información para brindar las funciones principales de Intiva, personalizar la experiencia, mostrar estadísticas, organizar movimientos financieros, mejorar el servicio y mantener la seguridad de la cuenta."
            )

            PrivacySection(
                title = "4. Datos sensibles",
                body = "Debido a que Intiva trabaja con información financiera personal y familiar, tratamos estos datos con cuidado y únicamente para los fines relacionados con el funcionamiento de la aplicación."
            )

            PrivacySection(
                title = "5. Seguridad de la información",
                body = "Intiva busca aplicar buenas prácticas de seguridad para proteger la información del usuario. Sin embargo, el usuario también debe cuidar sus credenciales, evitar compartir su contraseña y cerrar sesión en dispositivos no confiables."
            )

            PrivacySection(
                title = "6. Funciones familiares o compartidas",
                body = "Si utilizas funciones familiares o grupales, cierta información podría ser visible para miembros autorizados del grupo, según las funcionalidades y permisos disponibles dentro de la aplicación."
            )

            PrivacySection(
                title = "7. Conservación de datos",
                body = "La información puede conservarse mientras la cuenta esté activa o mientras sea necesaria para prestar el servicio, mantener registros operativos, resolver incidencias o cumplir con necesidades técnicas del proyecto."
            )

            PrivacySection(
                title = "8. Responsabilidad del usuario",
                body = "El usuario es responsable de la veracidad de los datos ingresados y del uso adecuado de la aplicación. Intiva no se responsabiliza por información incorrecta, incompleta o ingresada de forma indebida."
            )

            PrivacySection(
                title = "9. Servicios externos",
                body = "Algunas funciones pueden apoyarse en servicios externos para autenticación, almacenamiento, notificaciones, imágenes u otros procesos técnicos necesarios para el funcionamiento de la app."
            )

            PrivacySection(
                title = "10. Cambios en esta política",
                body = "Esta Política de Privacidad puede actualizarse cuando se agreguen nuevas funciones, mejoras de seguridad o cambios en el tratamiento de información. Los cambios relevantes podrán comunicarse dentro de la aplicación."
            )

            PrivacySection(
                title = "11. Contacto",
                body = "Si tienes preguntas sobre esta Política de Privacidad, puedes comunicarte con el equipo responsable del proyecto Intiva mediante los canales definidos por la aplicación."
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PrivacySummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = IntivaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Resumen importante",
                color = IntivaPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Intiva utiliza tus datos para crear tu cuenta, organizar tu información financiera y ofrecer funciones de control personal o familiar. La app puede manejar información sensible, por eso debes usarla de forma responsable y proteger tu cuenta.",
                color = IntivaTextSecondary,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun PrivacySection(
    title: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = IntivaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                color = IntivaTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = body,
                color = IntivaTextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    HorizontalDivider(
        color = IntivaDivider.copy(alpha = 0.45f),
        thickness = 1.dp
    )

    Spacer(modifier = Modifier.height(12.dp))
}