package com.resolum.intiva.features.iam.presentation.signup

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.iam.domain.models.SignUpResult

/**
 * Represents the UI state for the Sign-Up screen.
 *
 * @property email The current email input by the user.
 * @property password The current password input by the user.
 * @property emailError An optional error message related to the email input.
 * @property passwordError An optional error message related to the password input.
 * @property signUpState The current state of the sign-up process, which can be idle, loading, success, or error.
 */
data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val signUpState: UiState<SignUpResult> = UiState.Idle
)