package com.resolum.intiva.features.iam.presentation.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.components.IntivaBackButton
import com.resolum.intiva.core.ui.components.IntivaPrimaryButton
import com.resolum.intiva.core.ui.components.IntivaTermsCheckbox
import com.resolum.intiva.core.ui.components.IntivaTextField
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Screen for user registration, allowing new users to create an account by providing their email and password.
 * Includes form validation, error handling, and navigation options to login and terms of service.
 *
 * @param onSignUpSuccess Callback triggered when the sign-up process is successful, typically used for navigation.
 * @param onNavigateBack Callback for navigating back to the previous screen.
 * @param onNavigateToLogin Callback for navigating to the login screen.
 */
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.signUpState) {
        if (uiState.signUpState is UiState.Success) onSignUpSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IntivaColors.BackgroundLavender),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(56.dp))

            IntivaBackButton(onClick = onNavigateBack)

            Spacer(Modifier.height(28.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { -20 },
            ) {
                Column {
                    Text(
                        text = "Crea tu cuenta",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = IntivaColors.TextPrimary,
                            lineHeight = 38.sp,
                        ),
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Comienza a construir el futuro financiero de tu familia hoy mismo.",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = IntivaColors.TextSecondary,
                            lineHeight = 22.sp,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = IntivaColors.SurfaceWhite,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {

                    IntivaTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Correo Electrónico",
                        leadingIcon = Icons.Outlined.Email,
                        isValid = uiState.email.contains("@") && uiState.emailError == null,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(20.dp))

                    IntivaTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Contraseña",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = if (passwordVisible)
                            Icons.Outlined.VisibilityOff
                        else
                            Icons.Outlined.Visibility,
                        onTrailingClick = { passwordVisible = !passwordVisible },
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(24.dp))


                    IntivaTermsCheckbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        fullText = "Acepto los Términos y Condiciones y la Política de Privacidad de Intiva.",
                        highlightedPhrases = listOf("Términos y Condiciones", "Política de Privacidad"),
                        onLinkClick = {

                        },
                    )

                    Spacer(Modifier.height(28.dp))


                    AnimatedVisibility(visible = uiState.signUpState is UiState.Error) {
                        Column {
                            Text(
                                text = (uiState.signUpState as? UiState.Error)?.message.orEmpty(),
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = IntivaColors.ErrorRed,
                                ),
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    IntivaPrimaryButton(
                        text = "Crear cuenta",
                        onClick = viewModel::onSignUpClick,
                        isLoading = uiState.signUpState is UiState.Loading,
                        enabled = termsAccepted,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                val loginText = buildAnnotatedString {
                    withStyle(SpanStyle(color = IntivaColors.TextSecondary, fontSize = 14.sp)) {
                        append("¿Ya tienes una cuenta? ")
                    }
                    withStyle(
                        SpanStyle(
                            color = IntivaColors.TextLink,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("Inicia sesión")
                    }
                }

                Text(
                    text = loginText,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNavigateToLogin,
                    ),
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
