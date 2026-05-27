package com.resolum.intiva.features.finances.presentation.transactions.components


private const val MAX_AMOUNT_LENGTH = 9
private const val MAX_DECIMAL_PLACES = 2

/**
 * Utility object for handling amount input logic in the transaction form.
 * This includes appending digits, deleting digits, and formatting the amount string.
 */
object AmountInput {

    /**
     * Appends a digit to the current amount string while ensuring proper formatting.
     *
     * @param current The current amount string (e.g., "0.00").
     * @param digit The digit to append (e.g., "1", "2", etc.).
     * @return The updated amount string with the new digit appended.
     */
    fun appendDigit(current: String, digit: String): String {
        if (current == "0.00") return digit

        val parts = current.split(".")
        val integerPart = parts[0]
        val decimalPart = parts.getOrNull(1) ?: ""

        if (parts.size == 2 && decimalPart.length >= MAX_DECIMAL_PLACES) {
            return current
        }

        if (parts.size == 1 && integerPart.length >= MAX_AMOUNT_LENGTH) {
            return current
        }

        return current + digit
    }

    /**
     * Deletes the last digit from the current amount string while ensuring proper formatting.
     *
     * @param current The current amount string (e.g., "12.34").
     * @return The updated amount string with the last digit removed, or "0.00" if all digits are deleted.
     */
    fun deleteDigit(current: String): String {
        val clean = current.replace(".", "")

        if (clean.length <= 1) return "0.00"

        val newClean = clean.dropLast(1)

        return formatAmount(newClean)
    }

    /**
     * Formats a string of digits into a proper amount format (e.g., "1234" -> "12.34").
     *
     * @param digits The string of digits to format.
     * @return The formatted amount string with two decimal places.
     */
    fun formatAmount(digits: String): String {
        val safe = digits.padStart(3, '0')

        val integerPart = safe.dropLast(2)
        val decimalPart = safe.takeLast(2)

        return "${integerPart.toInt()}.$decimalPart"
    }
}