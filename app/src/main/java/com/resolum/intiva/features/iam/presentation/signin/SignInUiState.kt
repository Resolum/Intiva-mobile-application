package com.resolum.intiva.features.iam.presentation.signin

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.iam.domain.models.SignInResult

/**
 * Represents the UI state for the Sign-In screen.
 *
 * @property email The current email input by the user.
 * @property password The current password input by the user.
 * @property emailError An optional error message related to the email input.
 * @property passwordError An optional error message related to the password input.
 * @property signInState The current state of the sign-in process, which can be idle, loading, success, or error.
 */
data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val signInState: UiState<SignInResult> = UiState.Idle
)
