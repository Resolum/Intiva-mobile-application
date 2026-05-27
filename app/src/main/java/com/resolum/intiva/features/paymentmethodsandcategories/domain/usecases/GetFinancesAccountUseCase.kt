package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

/**
 * Use case for fetching financial accounts associated with the user. This use case interacts with the FinancialAccountRepository
 * to retrieve the list of financial accounts based on the user's ID.
 *
 * The use case is designed to be invoked from the presentation layer (e.g., ViewModel) to fetch financial accounts and handle
 * the result, which can be a success with a list of financial accounts or an error with an appropriate message and throwable.
 */
class GetFinancesAccountUseCase @Inject constructor(
    private val repository: FinancialAccountRepository
) {
    suspend operator fun invoke(): NetworkResult<List<FinancialAccount>> =
        repository.getFinancialAccountsByUserId()
}