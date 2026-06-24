package com.resolum.intiva.features.paymentmethodsandcategories.data.remote

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateFinancialAccountRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.UpdateFinancialAccountRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.FinancialAccountService
import javax.inject.Inject

class FinancialAccountFacadeService @Inject constructor(
    private val financialAccountService: FinancialAccountService
) {
    suspend fun getFinancialAccountsByUserId(userId: Long) =
        financialAccountService.getFinancialAccountsByUserId(userId)

    suspend fun createFinancialAccount(
        userId: Long,
        request: CreateFinancialAccountRequestDto
    ) = financialAccountService.createFinancialAccount(
        userId = userId,
        request = request
    )

    suspend fun updateFinancialAccount(
        userId: Long,
        accountId: Long,
        request: UpdateFinancialAccountRequestDto
    ) = financialAccountService.updateFinancialAccount(
        userId = userId,
        accountId = accountId,
        request = request
    )
}
