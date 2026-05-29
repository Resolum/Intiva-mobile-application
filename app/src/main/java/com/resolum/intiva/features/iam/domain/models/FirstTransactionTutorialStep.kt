package com.resolum.intiva.features.iam.domain.models

/**
 * Enum class representing the steps of the first transaction tutorial in the IAM (Identity and Access Management) feature.
 *
 * @property OPEN_CREATE_TRANSACTION The step where the user is guided to open the create transaction screen.
 * @property SELECT_CATEGORY The step where the user is guided to select a category for the transaction.
 * @property ENTER_AMOUNT The step where the user is guided to enter the amount for the transaction.
 * @property CONFIRM_TRANSACTION The step where the user is guided to confirm the transaction details before saving.
 * @property COMPLETED The step indicating that the user has completed the first transaction tutorial.
 */
enum class FirstTransactionTutorialStep {
    OPEN_CREATE_TRANSACTION,
    SELECT_CATEGORY,
    ENTER_AMOUNT,
    CONFIRM_TRANSACTION,
    COMPLETED
}