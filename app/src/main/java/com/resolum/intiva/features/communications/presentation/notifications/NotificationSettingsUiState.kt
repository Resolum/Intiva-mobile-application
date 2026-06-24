package com.resolum.intiva.features.communications.presentation.notifications

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

data class NotificationSection(
    val title: String,
    val icon: ImageVector,
    val items: List<NotificationSettingDisplayItem>
)

data class NotificationSettingDisplayItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val enabled: Boolean
)

data class SectionInfo(
    val title: String,
    val icon: ImageVector
)

val SECTIONS = listOf(
    SectionInfo(title = "INFORMACIÓN Y ACTUALIZACIONES", icon = Icons.Default.Info),
    SectionInfo(title = "ACTIVIDAD FINANCIERA", icon = Icons.Default.AccountBalance),
    SectionInfo(title = "SEGURIDAD", icon = Icons.Default.Lock),
    SectionInfo(title = "ACTIVIDAD FAMILIAR", icon = Icons.Default.Groups)
)

val DEFAULT_SETTINGS = listOf(
    NotificationSettingDisplayItem(
        id = "APP_UPDATES", title = "Novedades de la aplicación",
        subtitle = "Nuevas funciones, mejoras y consejos para aprovechar al máximo tu cuenta.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "PROMOTIONS", title = "Ofertas y Promociones",
        subtitle = "Beneficios exclusivos, tasas preferenciales y recompensas para tu familia.",
        enabled = false
    ),
    NotificationSettingDisplayItem(
        id = "TRANSFERS", title = "Transferencias e Ingresos",
        subtitle = "Alertas instantáneas cuando recibas dinero o depósitos en tu cuenta principal.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "SCHEDULED_PAYMENTS", title = "Pagos Programados",
        subtitle = "Recordatorios antes de que se ejecuten cobros recurrentes o suscripciones.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "LOW_BALANCE", title = "Alerta de Saldo Bajo",
        subtitle = "Te avisaremos si tu saldo principal cae por debajo de S/. 100.00.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "NEW_LOGINS", title = "Nuevos Inicios de Sesión",
        subtitle = "Alertas obligatorias cuando detectamos acceso desde un dispositivo nuevo.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "ACCOUNT_CHANGES", title = "Cambios en la Cuenta",
        subtitle = "Notificaciones por actualizaciones de contraseña, PIN o datos personales.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "APPROVAL_REQUESTS", title = "Solicitudes de Aprobación",
        subtitle = "Avisos cuando un dependiente solicita fondos o permiso para un gasto.",
        enabled = true
    ),
    NotificationSettingDisplayItem(
        id = "GOAL_PROGRESS", title = "Progreso de Metas",
        subtitle = "Celebra cuando tu familia alcance hitos de ahorro compartidos.",
        enabled = true
    )
)

fun buildDefaultSections(): List<NotificationSection> {
    val typeToSection = mapOf(
        "APP_UPDATES" to 0, "PROMOTIONS" to 0,
        "TRANSFERS" to 1, "SCHEDULED_PAYMENTS" to 1, "LOW_BALANCE" to 1,
        "NEW_LOGINS" to 2, "ACCOUNT_CHANGES" to 2,
        "APPROVAL_REQUESTS" to 3, "GOAL_PROGRESS" to 3
    )

    val grouped = MutableList(SECTIONS.size) { mutableListOf<NotificationSettingDisplayItem>() }

    DEFAULT_SETTINGS.forEach { item ->
        val sectionIndex = typeToSection[item.id] ?: return@forEach
        grouped[sectionIndex].add(item)
    }

    return SECTIONS.mapIndexed { index, section ->
        NotificationSection(
            title = section.title,
            icon = section.icon,
            items = grouped[index]
        )
    }
}
