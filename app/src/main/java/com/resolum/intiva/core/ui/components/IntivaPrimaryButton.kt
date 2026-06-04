package com.resolum.intiva.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * IntivaPrimaryButton is a custom button component that follows the Intiva design system.
 *
 * @param text The text to display inside the button.
 * @param onClick The callback to invoke when the button is clicked.
 * @param modifier Optional [Modifier] for styling and layout.
 * @param isLoading If true, shows a loading indicator instead of the text and icon.
 * @param enabled If false, the button is disabled and shows a dimmed appearance.
 * @param showArrow If true, displays an arrow icon on the right side of the button.
 */
@Composable
fun IntivaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    showArrow: Boolean = true,
) {
    val bgColor by animateColorAsState(
        targetValue = if (enabled && !isLoading)
            IntivaColors.PrimaryGreen
        else
            IntivaColors.PrimaryGreen.copy(alpha = 0.5f),
        label = "buttonBg",
    )

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(horizontal = 28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = IntivaColors.TextPrimary,
            disabledContainerColor = IntivaColors.PrimaryGreen.copy(alpha = 0.5f),
            disabledContentColor = IntivaColors.TextPrimary.copy(alpha = 0.5f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.5.dp,
                color = IntivaColors.TextPrimary,
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Spacer igual al ancho del ícono para centrar el texto visualmente
                if (showArrow) Spacer(Modifier.width(24.dp))

                Text(
                    text = text.uppercase(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = IntivaColors.TextPrimary,
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )

                if (showArrow) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = IntivaColors.TextPrimary,
                    )
                }
            }
        }
    }
}
