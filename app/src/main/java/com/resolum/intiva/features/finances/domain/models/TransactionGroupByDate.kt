package com.resolum.intiva.features.finances.domain.models

/**
 * A data class representing a group of transactions that occurred on the same date.
 *
 * @property date The date of the transactions in the format "YYYY-MM-DD".
 * @property transactions A list of [Transaction] objects that occurred on the specified date.
 */
data class TransactionGroupByDate(
    val date: String,
    val transactions: List<TransactionWithDesignResponse>
)
