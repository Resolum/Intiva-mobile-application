package com.resolum.intiva.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Brand
private val IntivaGreen       = Color(0xFFB2F000)
private val IntivaGreenDark   = Color(0xFF8ABB00)
private val IntivaPurple      = Color(0xFF6B5CE7)
private val IntivaPurpleLight = Color(0xFF9B8FF2)

// Backgrounds — Light
private val BgLavender        = Color(0xFFF0EEF8)
private val BgWhite           = Color(0xFFFFFFFF)
private val BgField           = Color(0xFFECEAF5)

// Backgrounds — Dark
private val BgDark            = Color(0xFF12101E)
private val BgDarkSurface     = Color(0xFF1E1B30)
private val BgDarkField       = Color(0xFF2A2640)

// Text — Light
private val TextDark          = Color(0xFF1A1825)
private val TextMuted         = Color(0xFF7B7A8E)

// Text — Dark
private val TextLight         = Color(0xFFF0EEF8)
private val TextMutedDark     = Color(0xFF9997B0)

// Error
private val ErrorRed          = Color(0xFFE53935)
private val ErrorRedLight     = Color(0xFFFF6B6B)

private val LightColorScheme = lightColorScheme(
    primary          = IntivaPurple,
    onPrimary        = Color.White,
    primaryContainer = BgField,
    onPrimaryContainer = IntivaPurple,

    secondary        = IntivaGreen,
    onSecondary      = TextDark,
    secondaryContainer = Color(0xFFDEF5A0),
    onSecondaryContainer = Color(0xFF2D4A00),

    tertiary         = IntivaPurple,
    onTertiary       = Color.White,

    background       = BgLavender,
    onBackground     = TextDark,

    surface          = BgWhite,
    onSurface        = TextDark,
    surfaceVariant   = BgField,
    onSurfaceVariant = TextMuted,

    outline          = Color(0xFFCAC8DC),
    outlineVariant   = IntivaPurple,

    error            = ErrorRed,
    onError          = Color.White,
    errorContainer   = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),

    scrim            = Color(0xFF000000),
)

private val DarkColorScheme = darkColorScheme(
    primary          = IntivaPurpleLight,
    onPrimary        = Color(0xFF1A1825),
    primaryContainer = BgDarkField,
    onPrimaryContainer = IntivaPurpleLight,

    secondary        = IntivaGreenDark,
    onSecondary      = Color(0xFF1A1825),
    secondaryContainer = Color(0xFF3A5200),
    onSecondaryContainer = Color(0xFFBEF264),

    tertiary         = IntivaPurpleLight,
    onTertiary       = Color(0xFF1A1825),

    background       = BgDark,
    onBackground     = TextLight,

    surface          = BgDarkSurface,
    onSurface        = TextLight,
    surfaceVariant   = BgDarkField,
    onSurfaceVariant = TextMutedDark,

    outline          = Color(0xFF4A4760),
    outlineVariant   = IntivaPurpleLight,

    error            = ErrorRedLight,
    onError          = Color(0xFF690005),
    errorContainer   = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    scrim            = Color(0xFF000000),
)

@Composable
fun IntivaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}