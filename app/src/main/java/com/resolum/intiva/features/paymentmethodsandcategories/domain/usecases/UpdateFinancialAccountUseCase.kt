package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

class UpdateFinancialAccountUseCase @Inject constructor(
    private val repository: FinancialAccountRepository
) {
    suspend operator fun invoke(
        accountId: Long,
        name: String? = null,
        isActive: Boolean? = null
    ): NetworkResult<FinancialAccount> {
        return repository.updateFinancialAccount(accountId, name, isActive)
    }
}
