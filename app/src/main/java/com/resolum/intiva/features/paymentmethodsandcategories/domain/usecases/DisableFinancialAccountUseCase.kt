package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

/**
 * Use case for disabling a financial account.
 *
 * @property financialAccountRepository Repository for financial account operations.
 */
class DisableFinancialAccountUseCase @Inject constructor(
    private val financialAccountRepository: FinancialAccountRepository
) {
    suspend operator fun invoke(accountId: Long): NetworkResult<FinancialAccount> =
        financialAccountRepository.disableFinancialAccount(accountId)
}