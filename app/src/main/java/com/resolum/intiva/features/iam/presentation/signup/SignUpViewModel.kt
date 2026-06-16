package com.resolum.intiva.features.iam.presentation.signup

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.ui.snackbar.SnackBarBus
import com.resolum.intiva.core.ui.snackbar.SnackBarType
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the Sign-Up screen.
 *
 * This class is responsible for:
 * - Managing the sign-up UI state.
 * - Receiving user input from the screen.
 * - Validating the registration form.
 * - Sending the sign-up request to the domain layer through [SignUpUseCase].
 *
 * @property signUpUseCase Use case that handles the sign-up process.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {

    /**
     * Private mutable state used internally by the ViewModel.
     */
    private val _uiState = MutableStateFlow(SignUpUiState())

    /**
     * Public immutable state observed by the Sign-Up screen.
     */
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    /**
     * Updates the email input and clears the email error.
     *
     * @param email New email entered by the user.
     */
    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null
            )
        }
    }

    /**
     * Updates the password input and clears the password error.
     *
     * @param password New password entered by the user.
     */
    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null
            )
        }
    }

    /**
     * Updates the name input and clears the name error.
     *
     * @param name New full name entered by the user.
     */
    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = null
            )
        }
    }

    /**
     * Updates the age input and clears the age error.
     *
     * Only numeric characters are allowed because age must be sent as an integer.
     *
     * @param age New age entered by the user.
     */
    fun onAgeChange(age: String) {
        _uiState.update {
            it.copy(
                age = age.filter { char -> char.isDigit() },
                ageError = null
            )
        }
    }

    /**
     * Updates the phone number input and clears the phone number error.
     *
     * @param phoneNumber New phone number entered by the user.
     */
    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                phoneNumberError = null
            )
        }
    }

    /**
     * Updates the biography input and clears the biography error.
     *
     * @param bio New biography entered by the user.
     */
    fun onBioChange(bio: String) {
        _uiState.update {
            it.copy(
                bio = bio,
                bioError = null
            )
        }
    }

    /**
     * Handles the sign-up button click.
     *
     * This method:
     * - Validates all required fields.
     * - Shows field errors if something is invalid.
     * - Sends the complete sign-up request if the form is valid.
     * - Updates the UI state depending on the result.
     */
    fun onSignUpClick() {
        safeLaunch {
            val state = uiState.value
            val ageValue = state.age.toIntOrNull()

            val emailError = when {
                state.email.isBlank() -> "El correo es obligatorio"
                !state.email.contains("@") -> "Ingresa un correo válido"
                else -> null
            }

            val passwordError = when {
                state.password.isBlank() -> "La contraseña es obligatoria"
                state.password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
                else -> null
            }

            val nameError = when {
                state.name.isBlank() -> "El nombre es obligatorio"
                else -> null
            }

            val ageError = when {
                state.age.isBlank() -> "La edad es obligatoria"
                ageValue == null -> "Ingresa una edad válida"
                ageValue <= 0 -> "Ingresa una edad válida"
                else -> null
            }

            val phoneNumberError = when {
                state.phoneNumber.isBlank() -> "El número de teléfono es obligatorio"
                state.phoneNumber.length < 9 -> "Ingresa un número de teléfono válido"
                else -> null
            }

            val bioError = when {
                state.bio.isBlank() -> "La biografía es obligatoria"
                else -> null
            }

            val hasErrors = listOf(
                emailError,
                passwordError,
                nameError,
                ageError,
                phoneNumberError,
                bioError
            ).any { it != null }

            if (hasErrors) {
                _uiState.update {
                    it.copy(
                        emailError = emailError,
                        passwordError = passwordError,
                        nameError = nameError,
                        ageError = ageError,
                        phoneNumberError = phoneNumberError,
                        bioError = bioError,
                        signUpState = UiState.Idle
                    )
                }
                return@safeLaunch
            }

            _uiState.update {
                it.copy(signUpState = UiState.Loading)
            }

            val result = signUpUseCase(
                SignUpRequest(
                    email = state.email.trim(),
                    password = state.password,
                    name = state.name.trim(),
                    age = ageValue ?: 0,
                    phoneNumber = state.phoneNumber.trim(),
                    bio = state.bio.trim()
                )
            )

            when (result) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            signUpState = UiState.Success(result.data)
                        )
                    }

                    SnackBarBus.send(
                        message = "Cuenta creada exitosamente",
                        type = SnackBarType.Success
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            signUpState = UiState.Error(result.message)
                        )
                    }

                    SnackBarBus.send(
                        message = "Error al crear la cuenta: ${result.message}",
                        type = SnackBarType.Error
                    )
                }
            }
        }
    }
}