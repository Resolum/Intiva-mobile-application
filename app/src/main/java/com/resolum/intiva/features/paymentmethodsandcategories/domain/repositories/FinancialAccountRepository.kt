package com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount

interface FinancialAccountRepository {
    suspend fun getFinancialAccountsByUserId(): NetworkResult<List<FinancialAccount>>

    suspend fun getFinancialAccountById(accountId: Long): FinancialAccount?

    suspend fun createFinancialAccount(
        name: String,
        accountType: String,
        currencyCode: String,
        currentAmount: Double,
        institution: String?,
        creditLimit: Double?
    ): NetworkResult<FinancialAccount>

    suspend fun updateFinancialAccount(
        accountId: Long,
        name: String? = null,
        isActive: Boolean? = null
    ): NetworkResult<FinancialAccount>
}
