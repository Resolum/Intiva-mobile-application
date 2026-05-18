package com.resolum.intiva.features.onboarding.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Custom Premium Compose Illustration for Slide 1 (Dashboard Card)
 */
@Composable
fun PremiumDashboardIllustration() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16152B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WealthHub",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "$48,210.35",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.PrimaryGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height * 0.7f)
                    cubicTo(
                        size.width * 0.25f, size.height * 0.85f,
                        size.width * 0.45f, size.height * 0.15f,
                        size.width * 0.65f, size.height * 0.35f
                    )
                    cubicTo(
                        size.width * 0.8f, size.height * 0.5f,
                        size.width * 0.9f, size.height * 0.1f,
                        size.width, size.height * 0.2f
                    )
                }

                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF534AB7).copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )

                drawPath(
                    path = path,
                    color = Color(0xFF6B5FD4),
                    style = Stroke(width = 3.5.dp.toPx())
                )


                drawCircle(
                    color = IntivaColors.PrimaryGreen,
                    radius = 5.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.65f, size.height * 0.35f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFFFF4B4B).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFFF4B4B), RoundedCornerShape(2.dp))
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Netflix",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                    Text(
                        text = "Suscripción mensual",
                        style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                    )
                }
                Text(
                    text = "- $12.99",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
            }
        }
    }
}

/**
 * Custom Premium Compose Illustration for Slide 2 (Concentric Saving Podiums)
 */
@Composable
fun PremiumGoalsIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f


            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = 125.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(cx, cy),
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = IntivaColors.PrimaryGreen.copy(alpha = 0.15f),
                radius = 95.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(cx, cy),
                style = Stroke(width = 2.dp.toPx())
            )


            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = 65.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(cx, cy + 40.dp.toPx())
            )


            val barWidth = 18.dp.toPx()
            val barSpacing = 22.dp.toPx()
            val startX = cx - (barWidth * 2f + barSpacing * 1.5f)

            val heights = listOf(35.dp.toPx(), 65.dp.toPx(), 110.dp.toPx(), 145.dp.toPx())
            heights.forEachIndexed { index, height ->
                val bx = startX + index * (barWidth + barSpacing)
                val by = cy + 40.dp.toPx() - height

                val barColor = if (index == 2) IntivaColors.PrimaryGreen else Color.White.copy(alpha = 0.25f)
                drawRoundRect(
                    color = barColor,
                    topLeft = androidx.compose.ui.geometry.Offset(bx, by),
                    size = androidx.compose.ui.geometry.Size(barWidth, height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f, barWidth / 2f)
                )
            }

            val arrowPath = Path().apply {
                moveTo(cx - 85.dp.toPx(), cy + 20.dp.toPx())
                cubicTo(
                    cx - 40.dp.toPx(), cy + 55.dp.toPx(),
                    cx + 10.dp.toPx(), cy - 20.dp.toPx(),
                    cx + 55.dp.toPx(), cy - 45.dp.toPx()
                )
            }

            drawPath(
                path = arrowPath,
                color = IntivaColors.PrimaryGreen,
                style = Stroke(width = 4.5.dp.toPx())
            )

            // Arrow Head pointing upwards
            val headPath = Path().apply {
                moveTo(cx + 55.dp.toPx(), cy - 45.dp.toPx())
                lineTo(cx + 42.dp.toPx(), cy - 43.dp.toPx())
                lineTo(cx + 53.dp.toPx(), cy - 32.dp.toPx())
                close()
            }
            drawPath(path = headPath, color = IntivaColors.PrimaryGreen)
        }
    }
}

/**
 * Custom Premium Compose Illustration for Slide 3 (Isometric Laptop Budget & Coins)
 */
@Composable
fun PremiumFamilyIllustration() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                val w = size.width
                val h = size.height


                val screenPath = Path().apply {
                    moveTo(w * 0.22f, h * 0.22f)
                    lineTo(w * 0.78f, h * 0.12f)
                    lineTo(w * 0.78f, h * 0.58f)
                    lineTo(w * 0.22f, h * 0.68f)
                    close()
                }
                drawPath(screenPath, color = Color(0xFF2C226B))


                drawRect(
                    color = Color(0xFF6B5FD4),
                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.32f),
                    size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 30.dp.toPx())
                )
                drawRect(
                    color = IntivaColors.PrimaryGreen,
                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.45f, h * 0.26f),
                    size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 42.dp.toPx())
                )
                drawRect(
                    color = Color(0xFF6B5FD4),
                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.55f, h * 0.36f),
                    size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 22.dp.toPx())
                )


                val keyboardPath = Path().apply {
                    moveTo(w * 0.22f, h * 0.68f)
                    lineTo(w * 0.78f, h * 0.58f)
                    lineTo(w * 0.9f, h * 0.82f)
                    lineTo(w * 0.1f, h * 0.92f)
                    close()
                }
                drawPath(keyboardPath, color = Color(0xFF1E193C))


                val trackpadPath = Path().apply {
                    moveTo(w * 0.45f, h * 0.82f)
                    lineTo(w * 0.55f, h * 0.8f)
                    lineTo(w * 0.53f, h * 0.86f)
                    lineTo(w * 0.43f, h * 0.88f)
                    close()
                }
                drawPath(trackpadPath, color = Color.White.copy(alpha = 0.2f))

                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = 11.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(w * 0.25f, h * 0.82f)
                )
                drawCircle(
                    color = Color(0xFFDAA520),
                    radius = 9.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(w * 0.29f, h * 0.85f)
                )
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = 11.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.78f)
                )


                val shieldPath = Path().apply {
                    val scx = w * 0.75f
                    val scy = h * 0.35f
                    moveTo(scx, scy - 20.dp.toPx())
                    lineTo(scx + 18.dp.toPx(), scy - 25.dp.toPx())
                    lineTo(scx + 18.dp.toPx(), scy + 5.dp.toPx())
                    cubicTo(
                        scx + 18.dp.toPx(), scy + 18.dp.toPx(),
                        scx, scy + 28.dp.toPx(),
                        scx, scy + 28.dp.toPx()
                    )
                    cubicTo(
                        scx, scy + 28.dp.toPx(),
                        scx - 18.dp.toPx(), scy + 18.dp.toPx(),
                        scx - 18.dp.toPx(), scy + 5.dp.toPx()
                    )
                    lineTo(scx - 18.dp.toPx(), scy - 25.dp.toPx())
                    close()
                }
                drawPath(shieldPath, color = IntivaColors.PrimaryGreen)


                drawCircle(
                    color = Color.White,
                    radius = 5.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.35f)
                )
            }
        }
    }
}

/**
 * Indicators at the BOTTOM for Slides 1 & 3
 */
@Composable
fun BottomOnboardingIndicators(currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(6.dp)
                    .width(if (isSelected) 24.dp else 6.dp)
                    .background(
                        color = if (isSelected) IntivaColors.BackgroundPurple else Color(0xFFE2DDF0),
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

/**
 * Indicators at the TOP for Slide 2
 */
@Composable
fun TopOnboardingIndicators(currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(6.dp)
                    .width(if (isSelected) 24.dp else 6.dp)
                    .background(
                        color = if (isSelected) IntivaColors.PrimaryGreen else Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}
