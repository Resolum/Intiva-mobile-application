package com.resolum.intiva.features.finances.presentation.transactions.components

object AmountInput {

    private const val MAX_INTEGER_DIGITS = 7
    private const val MAX_DECIMAL_PLACES = 2

    fun appendDigit(current: String, digit: String): String {
        if (current == "0") return digit

        return if (current.contains(".")) {
            val decimalPart = current.substringAfter(".")
            if (decimalPart.length >= MAX_DECIMAL_PLACES) current
            else current + digit
        } else {
            if (current.length >= MAX_INTEGER_DIGITS) current
            else current + digit
        }
    }

    fun deleteDigit(current: String): String {
        if (current == "0") return "0"
        if (current.length == 1) return "0"
        return current.dropLast(1)
    }
}