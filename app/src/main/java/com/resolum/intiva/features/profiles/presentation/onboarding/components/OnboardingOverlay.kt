package com.resolum.intiva.features.profiles.presentation.onboarding.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.resolum.intiva.features.profiles.domain.models.FirstTransactionTutorialStep

/**
 * Composable function that displays an onboarding overlay based on the current step of the first transaction tutorial.
 *
 * @param step The current step in the first transaction tutorial, or null if no step is active.
 * @param incomeRect The rectangle area to highlight for the "Open Create Transaction" step.
 * @param categoryRect The rectangle area to highlight for the "Select Category" step.
 * @param amountRect The rectangle area to highlight for the "Enter Amount" step.
 * @param saveButtonRect The rectangle area to highlight for the "Confirm Transaction" step.
 * @param onNext A callback function to be invoked when the user proceeds to the next step of the tutorial.
 */
@Composable
fun OnboardingOverlay(
    step: FirstTransactionTutorialStep?,
    incomeRect: Rect?,
    categoryRect: Rect?,
    amountRect: Rect?,
    amountDisplayRect: Rect? = null,
    saveButtonRect: Rect?,
    onNext: () -> Unit,
) {
    when (step) {

        FirstTransactionTutorialStep.OPEN_CREATE_TRANSACTION -> {
            SpotlightOverlay(
                rect = incomeRect,
                title = "Registra tu primer ingreso",
                message = "Toca el botón Ingreso para comenzar a llevar el control de tus finanzas.",
                stepNumber = 2,
                totalSteps = 5,
                onNext = onNext
            )
        }

        FirstTransactionTutorialStep.SELECT_CATEGORY -> {
            SpotlightOverlay(
                rect = categoryRect,
                title = "¿De dónde viene el dinero?",
                message = "Selecciona una categoría que describa mejor este ingreso.",
                stepNumber = 3,
                totalSteps = 5,
                onNext = onNext
            )
        }

        FirstTransactionTutorialStep.ENTER_AMOUNT -> {
            EnterAmountOverlay(
                amountDisplayRect = amountDisplayRect,
                numpadRect = amountRect,
                continueButtonRect = saveButtonRect
            )
        }

        FirstTransactionTutorialStep.CONFIRM_TRANSACTION -> {
            SpotlightOverlay(
                rect = saveButtonRect,
                title = "¡Último paso!",
                message = "Toca el botón para registrar tu primera transacción.",
                stepNumber = 5,
                totalSteps = 5,
                onNext = onNext
            )
        }

        else -> Unit
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EnterAmountOverlay(
    amountDisplayRect: Rect?,
    numpadRect: Rect?,
    continueButtonRect: Rect?
) {
    if (numpadRect == null) return

    val density = LocalDensity.current.density
    val spotlightPadding = 8f * density
    val highlightedRects = listOfNotNull(amountDisplayRect, numpadRect, continueButtonRect)

    fun Rect.containsWithPadding(x: Float, y: Float): Boolean =
        x >= left - spotlightPadding &&
                x <= right + spotlightPadding &&
                y >= top - spotlightPadding &&
                y <= bottom + spotlightPadding

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .pointerInteropFilter { motionEvent ->
                val inSpotlight = highlightedRects.any {
                    it.containsWithPadding(motionEvent.x, motionEvent.y)
                }
                !inSpotlight
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            drawRect(Color.Black.copy(alpha = 0.58f))

            highlightedRects.forEach { rect ->
                val padding = 8.dp.toPx()
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(rect.left - padding, rect.top - padding),
                    size = Size(rect.width + padding * 2, rect.height + padding * 2),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    blendMode = BlendMode.Clear
                )
            }
        }
    }
}
