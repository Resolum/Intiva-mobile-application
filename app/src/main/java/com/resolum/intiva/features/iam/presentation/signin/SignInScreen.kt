package com.resolum.intiva.features.iam.presentation.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.ui.components.IntivaGoogleButton
import com.resolum.intiva.core.ui.components.IntivaPrimaryButton
import com.resolum.intiva.core.ui.components.IntivaTextField
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * Sign In screen composable. Handles user input for email and password, displays validation errors, and shows loading/error states during sign-in.
 *
 * @param onSignInSuccess Callback to navigate to the home screen upon successful sign-in.
 * @param onNavigateToSignUp Callback to navigate to the sign-up screen.
 * @param onForgotPassword Callback to navigate to the forgot password screen.
 * @param onGoogleSignIn Callback to trigger Google Sign-In flow.
 */
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.signInState) {
        if (uiState.signInState is UiState.Success) onSignInSuccess()
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
                    .background(IntivaColors.BackgroundPurple),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
                    .background(IntivaColors.BackgroundLavender),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Intiva",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = IntivaColors.SurfaceWhite,
                    ),
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = IntivaColors.SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    Text(
                        text = "Bienvenido",
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = IntivaColors.TextPrimary,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Accede a tu cuenta familiar",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = IntivaColors.TextSecondary,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(28.dp))

                    IntivaTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Correo electrónico",
                        leadingIcon = Icons.Outlined.Email,
                        isValid = uiState.email.contains("@") && uiState.emailError == null,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(16.dp))

                    IntivaTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Contraseña",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        onTrailingClick = { passwordVisible = !passwordVisible },
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = IntivaColors.TextLink,
                                fontWeight = FontWeight.Medium,
                            ),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onForgotPassword,
                            ),
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    AnimatedVisibility(visible = uiState.signInState is UiState.Error) {
                        Column {
                            Text(
                                text = (uiState.signInState as? UiState.Error)?.message.orEmpty(),
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = IntivaColors.ErrorRed,
                                ),
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    IntivaPrimaryButton(
                        text = "Iniciar Sesión",
                        onClick = viewModel::onSignInClick,
                        isLoading = uiState.signInState is UiState.Loading,
                        enabled = uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = IntivaColors.TextSecondary.copy(alpha = 0.3f),
                        )
                        Text(
                            text = "  o continuar con  ",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = IntivaColors.TextSecondary,
                            ),
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = IntivaColors.TextSecondary.copy(alpha = 0.3f),
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    IntivaGoogleButton(
                        onClick = onGoogleSignIn,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = IntivaColors.TextSecondary, fontSize = 14.sp)) {
                            append("¿No tienes una cuenta? ")
                        }
                        withStyle(
                            SpanStyle(
                                color = IntivaColors.TextLink,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                            )
                        ) {
                            append("Regístrate aquí")
                        }
                    },
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onNavigateToSignUp,
                    ),
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}