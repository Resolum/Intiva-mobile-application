package com.resolum.intiva.features.paymentmethodsandcategories.data.remote

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.FinancialAccountService
import javax.inject.Inject

/**
 * Facade service for financial account-related operations.
 *
 * This service abstracts the underlying financial account data retrieval mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles fetching financial accounts based on user ID by
 * delegating to the appropriate services.
 */
class FinancialAccountFacadeService @Inject constructor(
    private val financialAccountService: FinancialAccountService
) {
    /**
     * Retrieves a list of financial accounts associated with the specified [userId].
     *
     * @param userId The ID of the user for whom to fetch financial accounts.
     * @return A list of financial accounts associated with the specified user ID.
     */
    suspend fun getFinancialAccountsByUserId(userId: Long) = financialAccountService.getFinancialAccountsByUserId(userId)
}