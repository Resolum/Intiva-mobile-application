package com.resolum.intiva.features.paymentmethodsandcategories.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao.FinancialAccountDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.mappers.toDomain
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.mappers.toEntity
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.FinancialAccountFacadeService
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.mappers.toDomain
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateFinancialAccountRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

/**
 * Implementation of the [FinancialAccountRepository] interface that interacts with the [FinancialAccountFacadeService]
 * to fetch financial account data from the remote API. It also uses the [SessionRepository] to retrieve
 * the current user's session information, such as the user ID, which is required for fetching
 * financial accounts specific to the user.
 *
 * This repository handles the business logic for fetching financial accounts and converting them into
 * domain models that can be used by the rest of the application. It also manages error handling
 * and ensures that network calls are made safely using the base repository's functionality.
 */
class FinancialAccountRepositoryImpl @Inject constructor(
    private val financialAccountFacadeService: FinancialAccountFacadeService,
    private val sessionRepository: SessionRepository,
    private val financialAccountDao: FinancialAccountDao
) : BaseRepository(), FinancialAccountRepository {

    /**
     * Fetches the list of financial accounts associated with the current user. It retrieves the user ID
     * from the session repository and then calls the financial account facade service to get the accounts.
     * The result is mapped to a list of domain models and returned as a [NetworkResult].
     *
     * @return A [NetworkResult] containing a list of [FinancialAccount] objects on success, or an error message on failure.
     */
    override suspend fun getFinancialAccountsByUserId(): NetworkResult<List<FinancialAccount>> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        val localFinancialAccounts = financialAccountDao.getActiveAccounts(userId).map { it.toDomain() }

        val remoteResult = safeCall {
            val remoteFinancialAccounts = financialAccountFacadeService.getFinancialAccountsByUserId(userId)
            val remoteDomainAccounts = remoteFinancialAccounts.map { it.toDomain() }

            financialAccountDao.deleteByUserId(userId)
            financialAccountDao.insertAll(remoteFinancialAccounts.map { it.toEntity(userId) })

            val storedActiveAccounts = financialAccountDao.getActiveAccounts(userId).map { it.toDomain() }
            storedActiveAccounts.ifEmpty {
                remoteDomainAccounts
            }
        }

        return when (remoteResult) {
            is NetworkResult.Success -> remoteResult
            is NetworkResult.Error -> {
                val localFallbackAccounts = localFinancialAccounts.ifEmpty {
                    financialAccountDao.getAccountsByUserId(userId).map { it.toDomain() }
                }

                if (localFallbackAccounts.isNotEmpty()) {
                    NetworkResult.Success(localFallbackAccounts)
                } else {
                    remoteResult
                }
            }
        }
    }


    override suspend fun createFinancialAccount(
        name: String,
        accountType: String,
        currencyCode: String,
        currentAmount: Double,
        institution: String?,
        creditLimit: Double?
    ): NetworkResult<FinancialAccount> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        return safeCall {
            val request = CreateFinancialAccountRequestDto(
                name = name,
                accountType = accountType,
                currencyCode = currencyCode,
                currentAmount = currentAmount,
                institution = institution,
                creditLimit = creditLimit,
                isActive = true
            )

            val financialAccount = financialAccountFacadeService
                .createFinancialAccount(
                    userId = userId,
                    request = request
                )
                .toDomain()

            financialAccountDao.insert(financialAccount.toEntity(userId))
            financialAccount
        }
    }

    override suspend fun disableFinancialAccount(accountId: Long): NetworkResult<FinancialAccount> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        return safeCall {
            val financialAccount = financialAccountFacadeService
                .disableFinancialAccount(userId = userId, accountId = accountId)
                .toDomain()

            financialAccountDao.insert(financialAccount.toEntity(userId))
            financialAccount
        }
    }
}
