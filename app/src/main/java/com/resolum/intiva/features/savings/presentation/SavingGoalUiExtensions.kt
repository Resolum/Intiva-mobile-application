package com.resolum.intiva.features.savings.presentation

import com.resolum.intiva.features.savings.domain.models.SavingGoal
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/** Progress percentage (0–100) based on current vs target amount. */
fun SavingGoal.progressPercent(): Int {
    if (targetAmount <= BigDecimal.ZERO) return 0
    return currentAmount
        .divide(targetAmount, 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal(100))
        .toInt()
        .coerceIn(0, 100)
}

/** Formats a monetary amount for display in list cards. */
fun SavingGoal.formatAmount(amount: BigDecimal): String {
    val formatter = DecimalFormat("#,##0.##")
    return formatter.format(amount)
}
