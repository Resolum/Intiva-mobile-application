package com.resolum.intiva.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * A styled button for Google sign-in, matching the Intiva design system.
 * Displays the Google logo alongside "Google" text.
 *
 * @param onClick Callback triggered when the button is clicked.
 * @param modifier Modifier applied to the button.
 */
@Composable
fun IntivaGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE2DDF0)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = IntivaColors.TextPrimary,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        GoogleLogo(modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Google",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = IntivaColors.TextPrimary,
            ),
        )
    }
}

/**
 * Renders the Google logo dynamically using SVG vector path coordinates on Canvas.
 * This guarantees the logo looks crisp and exact at any scale or device resolution.
 */
@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f


        val bluePath = Path().apply {
            moveTo(23.49f * scaleX, 12.27f * scaleY)
            cubicTo(23.49f * scaleX, 11.46f * scaleY, 23.42f * scaleX, 10.68f * scaleY, 23.29f * scaleX, 9.92f * scaleY)
            lineTo(12f * scaleX, 9.92f * scaleY)
            lineTo(12f * scaleX, 14.36f * scaleY)
            lineTo(18.44f * scaleX, 14.36f * scaleY)
            cubicTo(18.16f * scaleX, 15.84f * scaleY, 17.32f * scaleX, 17.09f * scaleY, 16.06f * scaleX, 17.94f * scaleY)
            lineTo(16.06f * scaleX, 20.91f * scaleY)
            lineTo(19.9f * scaleX, 20.91f * scaleY)
            cubicTo(22.15f * scaleX, 18.84f * scaleY, 23.49f * scaleX, 15.8f * scaleY, 23.49f * scaleX, 12.27f * scaleY)
            close()
        }
        drawPath(bluePath, Color(0xFF4285F4))


        val greenPath = Path().apply {
            moveTo(12f * scaleX, 24f * scaleY)
            cubicTo(15.24f * scaleX, 24f * scaleY, 17.96f * scaleX, 22.93f * scaleY, 19.95f * scaleX, 21.09f * scaleY)
            lineTo(16.06f * scaleX, 18.12f * scaleY)
            cubicTo(14.99f * scaleX, 18.84f * scaleY, 13.63f * scaleX, 19.27f * scaleY, 11.89f * scaleX, 19.27f * scaleY)
            cubicTo(8.73f * scaleX, 19.27f * scaleY, 6.06f * scaleX, 17.13f * scaleY, 5.1f * scaleX, 14.25f * scaleY)
            lineTo(1.23f * scaleX, 14.25f * scaleY)
            lineTo(1.23f * scaleX, 17.32f * scaleY)
            cubicTo(3.21f * scaleX, 21.2f * scaleY, 7.26f * scaleX, 24f * scaleY, 12f * scaleX, 24f * scaleY)
            close()
        }
        drawPath(greenPath, Color(0xFF34A853))


        val yellowPath = Path().apply {
            moveTo(5.1f * scaleX, 14.25f * scaleY)
            cubicTo(4.86f * scaleX, 13.53f * scaleY, 4.72f * scaleX, 12.76f * scaleY, 4.72f * scaleX, 12f * scaleY)
            cubicTo(4.72f * scaleX, 11.24f * scaleY, 4.86f * scaleX, 10.47f * scaleY, 5.1f * scaleX, 9.75f * scaleY)
            lineTo(5.1f * scaleX, 6.68f * scaleY)
            lineTo(1.23f * scaleX, 6.68f * scaleY)
            cubicTo(0.48f * scaleX, 8.39f * scaleY, 0f * scaleX, 10.29f * scaleY, 0f * scaleX, 12f * scaleY)
            cubicTo(0f * scaleX, 13.71f * scaleY, 0.48f * scaleX, 15.61f * scaleY, 1.23f * scaleX, 17.32f * scaleY)
            lineTo(5.1f * scaleX, 14.25f * scaleY)
            close()
        }
        drawPath(yellowPath, Color(0xFFFBBC05))


        val redPath = Path().apply {
            moveTo(12f * scaleX, 4.75f * scaleY)
            cubicTo(13.76f * scaleX, 4.75f * scaleY, 15.34f * scaleX, 5.36f * scaleY, 16.59f * scaleX, 6.55f * scaleY)
            lineTo(19.95f * scaleX, 3.19f * scaleY)
            cubicTo(17.96f * scaleX, 1.19f * scaleY, 15.24f * scaleX, 0f * scaleX, 12f * scaleX, 0f * scaleX)
            cubicTo(7.26f * scaleX, 0f * scaleX, 3.21f * scaleX, 2.8f * scaleY, 1.23f * scaleX, 6.68f * scaleY)
            lineTo(5.1f * scaleX, 9.75f * scaleY)
            cubicTo(6.06f * scaleX, 6.87f * scaleY, 8.73f * scaleX, 4.75f * scaleY, 12f * scaleX, 4.75f * scaleY)
            close()
        }
        drawPath(redPath, Color(0xFFEA4335))
    }
}