package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

class CreateFinancialAccountUseCase @Inject constructor(
    private val financialAccountRepository: FinancialAccountRepository
) {
    suspend operator fun invoke(
        name: String,
        accountType: String,
        currencyCode: String,
        currentAmount: Double,
        institution: String?,
        creditLimit: Double?
    ) = financialAccountRepository.createFinancialAccount(
        name = name,
        accountType = accountType,
        currencyCode = currencyCode,
        currentAmount = currentAmount,
        institution = institution,
        creditLimit = creditLimit
    )
}