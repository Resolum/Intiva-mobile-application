package com.resolum.intiva.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * A custom text field component with built-in validation and error handling.
 *
 * @param value The current text value of the field.
 * @param onValueChange Callback when the text value changes.
 * @param label The label to display above the text field.
 * @param leadingIcon The icon to display at the start of the text field.
 * @param modifier Optional [Modifier] for styling.
 * @param trailingIcon Optional icon to display at the end of the text field (e.g., for actions).
 * @param onTrailingClick Optional callback when the trailing icon is clicked.
 * @param isValid Whether the current input is valid (shows a checkmark if true).
 * @param isError Whether there is an error with the input (shows error state if true).
 * @param errorMessage Optional error message to display below the field when in error state.
 * @param visualTransformation Optional visual transformation for password fields, etc.
 * @param keyboardOptions Optional keyboard options for input type, capitalization, etc.
 * @param placeholder Placeholder text to show when the field is empty.
 */
@Composable
fun IntivaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    onTrailingClick: (() -> Unit)? = null,
    isValid: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String = "",
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isError   -> IntivaColors.FieldBorderError
        isFocused -> IntivaColors.FieldBorderFocused
        else      -> Color.Transparent
    }

    Column(modifier = modifier) {

        Text(
            text = label,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isError) IntivaColors.ErrorRed else IntivaColors.TextSecondary,
            ),
            modifier = Modifier.padding(bottom = 6.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(IntivaColors.FieldBackground)
                .border(
                    width = 1.5.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp, vertical = 18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = IntivaColors.IconPurple,
                    modifier = Modifier.size(22.dp),
                )

                Spacer(Modifier.width(12.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = IntivaColors.TextPrimary,
                    ),
                    cursorBrush = SolidColor(IntivaColors.IconPurple),
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = IntivaColors.TextSecondary.copy(alpha = 0.5f),
                                    ),
                                )
                            }
                            innerTextField()
                        }
                    },
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isValid && !isError,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Válido",
                            tint = IntivaColors.CheckGreen,
                            modifier = Modifier.size(22.dp),
                        )
                    }

                    if (trailingIcon != null && !isValid) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = IntivaColors.TextSecondary,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable {
                                    onTrailingClick?.invoke()
                                }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = isError && errorMessage != null) {
            Text(
                text = errorMessage.orEmpty(),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = IntivaColors.ErrorRed,
                ),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }
}