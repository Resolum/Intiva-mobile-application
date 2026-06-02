package com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

/**
 * Repository interface for managing financial accounts, providing methods to fetch financial accounts associated with the user.
 *
 * This interface abstracts the data source for financial accounts, allowing for different implementations (e.g., remote API, local database)
 * without affecting the rest of the application. It defines a method to retrieve financial accounts based on the user's ID.
 */
interface FinancialAccountRepository {
    suspend fun getFinancialAccountsByUserId(): NetworkResult<List<FinancialAccount>>


    suspend fun createFinancialAccount(
        name: String,
        accountType: String,
        currencyCode: String,
        currentAmount: Double,
        institution: String?,
        creditLimit: Double?
    ): NetworkResult<FinancialAccount>


}