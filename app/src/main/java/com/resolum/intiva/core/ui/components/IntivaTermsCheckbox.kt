package com.resolum.intiva.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resolum.intiva.core.ui.theme.IntivaColors

/**
 * A custom checkbox component for accepting terms and conditions, with clickable highlighted phrases.
 *
 * @param checked Whether the checkbox is checked.
 * @param onCheckedChange Callback when the checkbox state changes.
 * @param modifier Optional [Modifier] for styling.
 * @param fullText The full text to display next to the checkbox.
 * @param highlightedPhrases List of phrases in [fullText] to highlight and make clickable.
 * @param onLinkClick Optional callback when a highlighted phrase is clicked.
 */
@Composable
fun IntivaTermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    fullText: String = "Acepto los Términos y Condiciones y la Política de Privacidad de Intiva.",
    highlightedPhrases: List<String> = listOf("Términos y Condiciones", "Política de Privacidad"),
    onLinkClick: (() -> Unit)? = null,
) {
    val annotated = buildAnnotatedString {
        var cursor = 0
        val remaining = fullText

        val sortedMatches = highlightedPhrases
            .flatMap { phrase ->
                val indices = mutableListOf<Pair<Int, String>>()
                var idx = remaining.indexOf(phrase, cursor)
                while (idx != -1) {
                    indices.add(idx to phrase)
                    idx = remaining.indexOf(phrase, idx + 1)
                }
                indices
            }
            .sortedBy { it.first }

        for ((start, phrase) in sortedMatches) {
            if (start > cursor) append(remaining.substring(cursor, start))
            withStyle(SpanStyle(color = IntivaColors.TextLink, fontWeight = FontWeight.SemiBold)) {
                append(phrase)
            }
            cursor = start + phrase.length
        }
        if (cursor < remaining.length) append(remaining.substring(cursor))
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(20.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = IntivaColors.IconPurple,
                uncheckedColor = IntivaColors.TextSecondary,
                checkmarkColor = IntivaColors.SurfaceWhite,
            ),
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = annotated,
            style = TextStyle(
                fontSize = 13.sp,
                color = IntivaColors.TextSecondary,
                lineHeight = 18.sp,
            ),
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onLinkClick?.invoke() },
            ),
        )
    }
}
