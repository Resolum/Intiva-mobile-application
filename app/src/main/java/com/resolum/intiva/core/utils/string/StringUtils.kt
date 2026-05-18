package com.resolum.intiva.core.utils.string

/** Returns true if this string is not null and not blank. */
fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()

/** Capitalizes the first letter of each word in the string. */
fun String.toTitleCase(): String =
    split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercaseChar() }
    }

/**
 * Truncates the string to [maxLength] characters, appending [ellipsis] if truncated.
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String =
    if (length <= maxLength) this else take(maxLength - ellipsis.length) + ellipsis

/** Returns this string or [fallback] if null or blank. */
fun String?.orFallback(fallback: String): String =
    if (isNullOrBlank()) fallback else this!!
