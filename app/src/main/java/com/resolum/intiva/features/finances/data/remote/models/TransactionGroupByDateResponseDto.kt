package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a response containing transactions grouped by date.
 *
 * @property date The date for which the transactions are grouped, formatted as a string (e.g., "2024-06-01").
 * @property transactions A list of transactions that occurred on the specified date.
 */
data class TransactionGroupByDateResponseDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("transactions")
    val transactions: List<TransactionWithDesignResponseDto>
)
