package com.resolum.intiva.features.finances.data.remote.mappers

import com.resolum.intiva.features.finances.data.remote.models.TransactionGroupByDateResponseDto
import com.resolum.intiva.features.finances.domain.models.TransactionGroupByDate

/**
 * Extension function to map a [TransactionGroupByDateResponseDto] to a [TransactionGroupByDate] domain model.
 *
 * This function takes a [TransactionGroupByDateResponseDto] object and converts it into a [TransactionGroupByDate] object
 * by mapping the date and transforming the list of transactions using the `toDomain()` function defined for each transaction.
 */
fun TransactionGroupByDateResponseDto.toDomain() = TransactionGroupByDate(
    date = date,
    transactions = transactions.map { it.toDomain() }
)