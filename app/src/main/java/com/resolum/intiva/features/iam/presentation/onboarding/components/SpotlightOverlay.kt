package com.resolum.intiva.features.iam.presentation.onboarding.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


/** * Composable function that displays a spotlight overlay highlighting a specific area of the screen, along with a tooltip message.
 *
 * @param rect The rectangle area to highlight with the spotlight effect. If null, the overlay will not be displayed.
 * @param message The message to display in the tooltip.
 * @param stepNumber The current step number in the onboarding process (default is 1).
 * @param totalSteps The total number of steps in the onboarding process (default is 4).
 * @param title The title to display in the tooltip.
 * @param onNext Callback function to be invoked when the user proceeds to the next step of the tutorial.
 * @param currentAmount An optional string representing the current amount entered by the user, used in specific steps of the tutorial.
 * @param onContinue An optional callback function to be invoked when the user clicks the "Continue" button in specific steps of the tutorial.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpotlightOverlay(
    rect: Rect?,
    message: String,
    stepNumber: Int = 1,
    totalSteps: Int = 4,
    title: String,
    onNext: () -> Unit,
    currentAmount: String? = null,
    onContinue: (() -> Unit)? = null
) {
    if (rect == null) return

    val density = LocalDensity.current.density
    val windowInfo = LocalWindowInfo.current
    val screenHeightPx = windowInfo.containerSize.height.toFloat()
    val spotlightPadding = 8f * density
    val elementIsInUpperHalf = (rect.center.y / density) < (screenHeightPx / density) / 2

    var tooltipRect by remember { mutableStateOf<Rect?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .pointerInteropFilter { motionEvent ->
                val x = motionEvent.x
                val y = motionEvent.y

                val inSpotlight =
                    x >= rect.left - spotlightPadding &&
                            x <= rect.right + spotlightPadding &&
                            y >= rect.top - spotlightPadding &&
                            y <= rect.bottom + spotlightPadding

                val inTooltip = tooltipRect?.let { t ->
                    x >= t.left && x <= t.right && y >= t.top && y <= t.bottom
                } ?: false

                !(inSpotlight || inTooltip)
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            drawRect(Color.Black.copy(alpha = 0.72f))
            val padding = 8.dp.toPx()
            drawRoundRect(
                color = Color.Transparent,
                topLeft = Offset(rect.left - padding, rect.top - padding),
                size = Size(rect.width + padding * 2, rect.height + padding * 2),
                cornerRadius = CornerRadius(12.dp.toPx()),
                blendMode = BlendMode.Clear
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(
                    if (elementIsInUpperHalf) Alignment.BottomCenter
                    else Alignment.TopCenter
                )
                .padding(
                    bottom = if (elementIsInUpperHalf) 48.dp else 0.dp,
                    top    = if (!elementIsInUpperHalf) 80.dp else 0.dp
                )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        tooltipRect = coordinates.boundsInRoot()
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Paso $stepNumber de $totalSteps",
                            fontSize = 12.sp,
                            color = Color(0xFF534AB7),
                            fontWeight = FontWeight.Medium
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(totalSteps) { index ->
                                Box(
                                    modifier = Modifier
                                        .height(4.dp)
                                        .width(if (index < stepNumber) 24.dp else 16.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(
                                            if (index < stepNumber) Color(0xFF534AB7)
                                            else Color(0xFFE0DEFC)
                                        )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF666680),
                        lineHeight = 20.sp
                    )

                    if (currentAmount != null) {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF2F0FA))
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S/. $currentAmount",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF534AB7)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    if (onContinue != null) {
                        Button(
                            onClick = onContinue,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF534AB7)
                            )
                        ) {
                            Text(
                                text = "Continuar →",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        val infiniteTransition = rememberInfiniteTransition(label = "arrow")
                        val arrowOffset by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 5f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600, easing = EaseInOut),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "arrowOffset"
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Toca el botón iluminado",
                                fontSize = 12.sp,
                                color = Color(0xFF534AB7),
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = if (elementIsInUpperHalf)
                                    Icons.Default.KeyboardArrowUp
                                else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color(0xFF534AB7),
                                modifier = Modifier
                                    .size(18.dp)
                                    .offset(y = (-arrowOffset).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}