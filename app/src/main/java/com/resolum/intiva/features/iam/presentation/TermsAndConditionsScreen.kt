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
private val IntivaSecondary = Color(0xFFCDEB45)
private val IntivaBackground = Color(0xFFFAF7FF)
private val IntivaSurface = Color.White
private val IntivaTextPrimary = Color(0xFF1F1B2D)
private val IntivaTextSecondary = Color(0xFF78767E)
private val IntivaDivider = Color(0xFFE9E6F2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = IntivaBackground,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = "Términos y Condiciones",
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
                text = "Términos y Condiciones de Intiva",
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

            SummaryCard()

            Spacer(modifier = Modifier.height(20.dp))

            TermsSection(
                title = "1. Aceptación de los términos",
                body = "Al crear una cuenta y utilizar Intiva, aceptas estos Términos y Condiciones. Si no estás de acuerdo con alguna parte de este documento, no deberías registrarte ni utilizar la aplicación."
            )

            TermsSection(
                title = "2. Finalidad de la aplicación",
                body = "Intiva es una aplicación orientada a la organización financiera personal y familiar. Permite registrar, consultar y administrar información relacionada con ingresos, gastos, categorías, métodos de pago, cuentas financieras, metas de ahorro y movimientos económicos."
            )

            TermsSection(
                title = "3. Cuenta de usuario",
                body = "Para acceder a determinadas funciones debes crear una cuenta con información veraz, actualizada y propia. Eres responsable de mantener la confidencialidad de tus credenciales de acceso y de las actividades realizadas desde tu cuenta."
            )

            TermsSection(
                title = "4. Datos personales y financieros",
                body = "Intiva puede procesar información personal y financiera ingresada por el usuario, como nombre, correo electrónico, foto de perfil, registros de ingresos, gastos, categorías, cuentas, metas de ahorro y preferencias de uso. Esta información se utiliza para brindar las funcionalidades principales de la aplicación."
            )

            TermsSection(
                title = "5. Responsabilidad sobre la información registrada",
                body = "El usuario es responsable de la exactitud, actualización y veracidad de los datos que registre en Intiva. La aplicación organiza y muestra información financiera, pero no garantiza que los datos ingresados por el usuario sean correctos."
            )

            TermsSection(
                title = "6. Seguridad y protección de la cuenta",
                body = "Intiva busca aplicar buenas prácticas de seguridad para proteger la información del usuario. Sin embargo, el usuario también debe proteger sus credenciales, evitar compartir su contraseña y cerrar sesión en dispositivos que no sean de confianza."
            )

            TermsSection(
                title = "7. Uso de información financiera",
                body = "La información presentada por Intiva tiene fines informativos, educativos y organizativos. Intiva no brinda asesoría financiera, contable, tributaria, legal ni de inversión. Las decisiones tomadas con base en la información mostrada son responsabilidad exclusiva del usuario."
            )

            TermsSection(
                title = "8. Funciones familiares o compartidas",
                body = "Si el usuario utiliza funciones familiares o compartidas, acepta que cierta información pueda ser visible para los miembros autorizados del grupo, familia o espacio compartido, según la configuración disponible en la aplicación."
            )

            TermsSection(
                title = "9. Métodos de pago y cuentas financieras",
                body = "Los métodos de pago, tarjetas, cuentas o referencias financieras registradas en Intiva se utilizan únicamente para fines de organización dentro de la aplicación. Intiva no debe interpretarse como una entidad bancaria, financiera, procesadora de pagos o billetera digital."
            )

            TermsSection(
                title = "10. Uso permitido",
                body = "El usuario se compromete a utilizar Intiva de forma lícita y responsable. Está prohibido utilizar la aplicación para actividades fraudulentas, ilegales, abusivas, manipulación de información, acceso no autorizado o cualquier acción que afecte la seguridad del sistema o de otros usuarios."
            )

            TermsSection(
                title = "11. Disponibilidad del servicio",
                body = "Intiva busca ofrecer un servicio estable y funcional. Sin embargo, la aplicación puede presentar interrupciones, errores, mantenimiento, actualizaciones o cambios en sus funcionalidades sin previo aviso."
            )

            TermsSection(
                title = "12. Limitación de responsabilidad",
                body = "Intiva no se hace responsable por pérdidas económicas, decisiones financieras incorrectas, errores de registro, uso inadecuado de la aplicación, fallas técnicas, pérdida de acceso o consecuencias derivadas del uso de la información ingresada por el usuario."
            )

            TermsSection(
                title = "13. Modificaciones de los términos",
                body = "Estos Términos y Condiciones pueden actualizarse en el futuro para reflejar cambios en la aplicación, mejoras de seguridad, nuevas funcionalidades o ajustes legales. Cuando existan cambios relevantes, podrán ser comunicados dentro de la aplicación o por los canales disponibles."
            )

            TermsSection(
                title = "14. Aceptación al registrarse",
                body = "Al marcar la casilla de aceptación durante el registro, confirmas que has leído, entendido y aceptado estos Términos y Condiciones, así como el tratamiento de la información necesaria para el funcionamiento de Intiva."
            )

            TermsSection(
                title = "15. Contacto",
                body = "Si tienes dudas sobre estos Términos y Condiciones, puedes comunicarte con el equipo responsable del proyecto Intiva mediante los canales definidos por la aplicación."
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = IntivaSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Resumen importante",
                color = IntivaPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Intiva organiza información financiera personal y familiar. Tus datos deben ser reales, seguros y usados de forma responsable. La aplicación no reemplaza asesoría financiera profesional.",
                color = IntivaTextSecondary,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Al continuar con el registro, aceptas el uso de tus datos necesarios para crear tu cuenta y utilizar las funciones de la aplicación.",
                color = IntivaTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun TermsSection(
    title: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = IntivaSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
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