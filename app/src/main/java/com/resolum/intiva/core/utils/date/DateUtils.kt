package com.resolum.intiva.core.utils.date

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val DEFAULT_PATTERN = "dd/MM/yyyy"
private const val DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm"
private const val GROUP_KEY_PATTERN = "yyyy-MM-dd"
private const val GROUP_LABEL_PATTERN = "EEEE, dd 'de' MMMM"

fun Long.toFormattedDate(pattern: String = DEFAULT_PATTERN): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}


/**
 * Converts a [Long] timestamp to a relative date-time string.
 * Examples:
 * - "Hoy, 10:30 AM"
 * - "Ayer, 09:15 PM"
 * - "26 May, 10:30 AM" (for dates older than yesterday)
 */
fun Long.toRelativeDateTime(): String {
    val now = Calendar.getInstance()
    val transactionDate = Calendar.getInstance().apply { timeInMillis = this@toRelativeDateTime }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val time = timeFormat.format(Date(this))

    return when {
        now.isSameDay(transactionDate) -> "Hoy, $time"
        now.isYesterday(transactionDate) -> "Ayer, $time"
        else -> {
            val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            dateFormat.format(Date(this))
        }
    }
}

/**
 * Converts an ISO date string to a relative date-time string.
 * Examples:
 * - "Hoy, 10:30 AM"
 * - "Ayer, 09:15 PM"
 * - "26 May, 10:30 AM"
 */
fun String.toRelativeDateTime(): String {
    return toBackendInstantOrNull()?.toEpochMilli()?.toRelativeDateTime() ?: this
}

private fun String.toBackendInstantOrNull(): Instant? {
    val value = trim()
    return runCatching {
        Instant.parse(value)
    }.getOrElse {
        runCatching {
            LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
                .atOffset(ZoneOffset.UTC)
                .toInstant()
        }.getOrNull()
    }
}

/**
 * Converts a [Long] timestamp to a group key string in the format "yyyy-MM-dd".
 * This is used for grouping transactions by date.
 */
private fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}

/**
 * Checks if the current calendar date is yesterday compared to another calendar date.
 *
 * @param other The calendar date to compare with.
 * @return `true` if the current calendar date is yesterday compared to the other date, `false` otherwise.
 */
private fun Calendar.isYesterday(other: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    return yesterday.isSameDay(other)
}
