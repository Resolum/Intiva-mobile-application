package com.resolum.intiva.features.iam.presentation.onboarding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import com.resolum.intiva.features.iam.domain.models.FirstTransactionTutorialStep

/**
 * Composable function that displays an onboarding overlay based on the current step of the first transaction tutorial.
 *
 * @param step The current step in the first transaction tutorial, or null if no step is active.
 * @param incomeRect The rectangle area to highlight for the "Open Create Transaction" step.
 * @param categoryRect The rectangle area to highlight for the "Select Category" step.
 * @param amountRect The rectangle area to highlight for the "Enter Amount" step.
 * @param saveButtonRect The rectangle area to highlight for the "Confirm Transaction" step.
 * @param currentAmount The current amount entered by the user, used in the "Enter Amount" step (optional).
 * @param onNext A callback function to be invoked when the user proceeds to the next step of the tutorial.
 */
@Composable
fun OnboardingOverlay(
    step: FirstTransactionTutorialStep?,
    incomeRect: Rect?,
    categoryRect: Rect?,
    amountRect: Rect?,
    saveButtonRect: Rect?,
    currentAmount: String? = null,
    onNext: () -> Unit,
) {
    when (step) {

        FirstTransactionTutorialStep.OPEN_CREATE_TRANSACTION -> {
            SpotlightOverlay(
                rect = incomeRect,
                title = "Registra tu primer ingreso",
                message = "Toca el botón Ingreso para comenzar a llevar el control de tus finanzas.",
                stepNumber = 1,
                totalSteps = 4,
                onNext = onNext
            )
        }

        FirstTransactionTutorialStep.SELECT_CATEGORY -> {
            SpotlightOverlay(
                rect = categoryRect,
                title = "¿De dónde viene el dinero?",
                message = "Selecciona una categoría que describa mejor este ingreso.",
                stepNumber = 2,
                totalSteps = 4,
                onNext = onNext
            )
        }

        FirstTransactionTutorialStep.ENTER_AMOUNT -> {
            SpotlightOverlay(
                rect = amountRect,
                title = "Ingresa el monto",
                message = "Escribe cuánto recibiste con el teclado.",
                stepNumber = 3,
                totalSteps = 4,
                onNext = onNext,
                currentAmount = currentAmount,
                onContinue = onNext
            )
        }

        FirstTransactionTutorialStep.CONFIRM_TRANSACTION -> {
            SpotlightOverlay(
                rect = saveButtonRect,
                title = "¡Último paso!",
                message = "Toca el botón para registrar tu primera transacción.",
                stepNumber = 4,
                totalSteps = 4,
                onNext = onNext
            )
        }

        else -> Unit
    }
}