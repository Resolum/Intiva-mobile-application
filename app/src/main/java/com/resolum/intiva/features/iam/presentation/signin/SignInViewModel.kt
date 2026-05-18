package com.resolum.intiva.features.iam.presentation.signin

import androidx.lifecycle.viewModelScope
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.ui.snackbar.SnackBarBus
import com.resolum.intiva.features.iam.domain.models.SignInRequest
import com.resolum.intiva.features.iam.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Sign-In screen, responsible for handling user input and managing the sign-in process.
 *
 * @property signInUseCase The use case for performing the sign-in operation, injected via Hilt.
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {

    /**
     * UI State for the Sign-In screen, holding the email, password, and the state of the sign-in process.
     */
    private val _uiState = MutableStateFlow(SignInUiState())

    /**
     * Publicly exposed StateFlow of the Sign-In UI state, allowing the UI to observe changes and update accordingly.
     */
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    /**
     * Update the email in the UI state and clear any existing email error.
     * @param email The new email entered by the user.
     */
    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null)
        }
    }

    /**
     * Update the password in the UI state and clear any existing password error.
     * @param password The new password entered by the user.
     */
    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null)
        }
    }

    /**
     * Handle the sign-in button click event. Validates the input and initiates the sign-in process.
     */
    fun onSignInClick() {
        safeLaunch {
            _uiState.update {
                it.copy(
                    signInState = UiState.Loading
                )
            }

            val state = uiState.value

            val result = signInUseCase(
                SignInRequest(
                    email = state.email,
                    password = state.password
                )
            )

            when ( result ) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            signInState = UiState.Success(result.data)
                        )
                    }

                    viewModelScope.launch {
                        SnackBarBus.send("Sign-In successful!")
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            signInState = UiState.Error(result.message)
                        )
                    }

                    viewModelScope.launch {
                        SnackBarBus.send("Error signing in: ${result.message}")
                    }
                }
            }
        }
    }
}