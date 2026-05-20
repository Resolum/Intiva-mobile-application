package com.resolum.intiva.features.iam.presentation.splash.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Composable for the content of the splash screen.
 *
 * @param progress The loading progress (0f to 1f) for the progress bar.
 */
@Composable
fun SplashScreenContent(
    progress: Float = 0f
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))


        Box(
            modifier = Modifier
                .size(110.dp)
                .graphicsLayer { rotationZ = 45f }
                .background(Color.White, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer { rotationZ = -45f } // Unrotate inner square
                    .background(IntivaColors.BackgroundPurple, shape = RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {

                Canvas(modifier = Modifier.size(28.dp)) {
                    val path = Path().apply {
                        moveTo(size.width * 0.55f, 0f)
                        lineTo(size.width * 0.15f, size.height * 0.55f)
                        lineTo(size.width * 0.45f, size.height * 0.55f)
                        lineTo(size.width * 0.35f, size.height * 1f)
                        lineTo(size.width * 0.85f, size.height * 0.45f)
                        lineTo(size.width * 0.55f, size.height * 0.45f)
                        close()
                    }
                    drawPath(path = path, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Intiva",
            style = TextStyle(
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Controla tus finanzas,\ntransforma tu vida",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        )

        Spacer(modifier = Modifier.weight(1.2f))


        Row(
            modifier = Modifier
                .width(72.dp)
                .height(5.dp)
                .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(2.5.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress)
                    .background(IntivaColors.PrimaryGreen, shape = RoundedCornerShape(2.5.dp))
            )
            Spacer(modifier = Modifier.weight(0.6f))
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}
