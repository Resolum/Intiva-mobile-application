package com.resolum.intiva.core.utils.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DEFAULT_PATTERN = "dd/MM/yyyy"
private const val DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm"

/**
 * Formats a [Long] timestamp to a human-readable date string.
 *
 * @param pattern Date pattern. Defaults to [DEFAULT_PATTERN].
 */
fun Long.toFormattedDate(pattern: String = DEFAULT_PATTERN): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}

/**
 * Formats a [Long] timestamp to a date-time string.
 */
fun Long.toFormattedDateTime(): String = toFormattedDate(DATE_TIME_PATTERN)

/**
 * Returns the current timestamp in milliseconds.
 */
fun currentTimeMillis(): Long = System.currentTimeMillis()
