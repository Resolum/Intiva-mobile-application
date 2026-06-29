package com.resolum.intiva.features.finances.presentation.transactions

import androidx.compose.ui.graphics.vector.ImageVector
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate
import com.resolum.intiva.features.finances.domain.models.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class FilterOption(val title: String, val type: TransactionType?) {
    ALL("Todos", null),
    INCOME("Ingresos", TransactionType.INCOME),
    EXPENSE("Gastos", TransactionType.EXPENSE)
}

enum class DateRangeOption(val title: String) {
    THIS_MONTH("Este mes"),
    LAST_MONTH("Mes pasado"),
    LAST_THREE_MONTHS("Ultimos 3 meses")
}

data class TransactionFilters(
    val dateRangeOption: DateRangeOption = DateRangeOption.THIS_MONTH,
    val fromDate: LocalDate? = YearMonth.now().atDay(1),
    val toDate: LocalDate? = LocalDate.now(),
    val type: FilterOption = FilterOption.ALL,
    val categoryIds: Set<Long> = emptySet()
)

data class FilterCategory(
    val id: Long,
    val name: String,
    val icon: ImageVector
)

fun List<TransactionGroupByDate>.filterWith(
    filters: TransactionFilters
): List<TransactionGroupByDate> {
    return flatMap { group ->
        val groupDate = group.date.toLocalDateOrNull()
        group.transactions.mapNotNull { transaction ->
            val transactionDate = transaction.registeredAt.toLocalDateOrNull() ?: groupDate
            val matchesDate = transactionDate == null ||
                ((filters.fromDate == null || !transactionDate.isBefore(filters.fromDate)) &&
                    (filters.toDate == null || !transactionDate.isAfter(filters.toDate)))
            val matchesCategory = filters.categoryIds.isEmpty() ||
                transaction.categoryId in filters.categoryIds

            if (matchesDate && matchesCategory) {
                (transactionDate ?: groupDate) to transaction
            } else {
                null
            }
        }
    }
        .groupBy { (date, _) -> date }
        .toSortedMap(compareByDescending { it })
        .map { (date, entries) ->
            TransactionGroupByDate(
                date = date?.toString().orEmpty(),
                transactions = entries.map { (_, transaction) -> transaction }
            )
        }
}

fun DateRangeOption.toDates(): Pair<LocalDate, LocalDate> {
    val today = LocalDate.now()
    return when (this) {
        DateRangeOption.THIS_MONTH -> YearMonth.from(today).atDay(1) to today
        DateRangeOption.LAST_MONTH -> {
            val lastMonth = YearMonth.from(today).minusMonths(1)
            lastMonth.atDay(1) to lastMonth.atEndOfMonth()
        }
        DateRangeOption.LAST_THREE_MONTHS -> today.minusMonths(3) to today
    }
}

fun String.toLocalDateOrNull(): LocalDate? {
    val value = trim()
    return runCatching {
        Instant.parse(value).atZone(ZoneId.systemDefault()).toLocalDate()
    }.getOrElse {
        runCatching {
            LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
                .atOffset(ZoneOffset.UTC)
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDate()
        }.getOrElse {
            runCatching { LocalDate.parse(value.take(10)) }.getOrNull()
        }
    }
}

fun LocalDate.toDatePickerMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun Long.toDatePickerLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

fun LocalDate.formatFilterDate(): String {
    return format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.forLanguageTag("es-PE")))
        .replace(".", "")
        .replaceFirstChar { it.uppercase() }
}
