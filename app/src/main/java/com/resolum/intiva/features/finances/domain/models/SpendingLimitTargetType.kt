package com.resolum.intiva.features.finances.domain.models

/**
 * Supported targets controlled by spending limits.
 */
object SpendingLimitTargetType {
    const val CATEGORY = "CATEGORY"
    const val FINANCIAL_ACCOUNT = "FINANCIAL_ACCOUNT"

    fun isSupported(targetType: String): Boolean =
        targetType.equals(CATEGORY, ignoreCase = true) ||
            targetType.equals(FINANCIAL_ACCOUNT, ignoreCase = true)
}
