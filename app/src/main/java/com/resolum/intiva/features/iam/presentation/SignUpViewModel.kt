package com.resolum.intiva.features.iam.presentation

import androidx.lifecycle.viewModelScope
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.ui.snackbar.SnackBarBus
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de registro de usuario.
 *
 * @property signUpUseCase Caso de uso para registrar un nuevo usuario.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {

    /**
     * UI State for the SignUp screen, holding the email, password, and the state of the sign-up process.
     */
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    /**
     * Update the email in the UI state and clear any existing email error.
     * @param email The new email entered by the user.
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    /**
     * Update the password in the UI state and clear any existing password error.
     * @param password The new password entered by the user.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    /**
     * Handle the sign-up button click event. Validates the input and initiates the sign-up process.
     */
    fun onSignUpClick() {
        safeLaunch {
            _uiState.update {
                it.copy(
                    signUpState = UiState.Loading
                )
            }

            val state = uiState.value

            val result = signUpUseCase(
                SignUpRequest(
                    email = state.email,
                    password = state.password
                )
            )

            when (result) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            signUpState = UiState.Success(result.data)
                        )
                    }

                    viewModelScope.launch {
                        SnackBarBus.send("Cuenta creada exitosamente")
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            signUpState = UiState.Error(result.message)
                        )
                    }

                    viewModelScope.launch {
                        SnackBarBus.send("Error al crear la cuenta: ${result.message}")
                    }
                }
            }
        }
    }
}