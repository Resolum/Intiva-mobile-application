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
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.UpdateFinancialAccountRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.FinancialAccount
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import javax.inject.Inject

class FinancialAccountRepositoryImpl @Inject constructor(
    private val financialAccountFacadeService: FinancialAccountFacadeService,
    private val sessionRepository: SessionRepository,
    private val financialAccountDao: FinancialAccountDao
) : BaseRepository(), FinancialAccountRepository {

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

    override suspend fun getFinancialAccountById(accountId: Long): FinancialAccount? {
        val userId = sessionRepository.getUserId() ?: return null
        return financialAccountDao.getById(accountId)?.toDomain()
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

    override suspend fun updateFinancialAccount(
        accountId: Long,
        name: String?,
        isActive: Boolean?
    ): NetworkResult<FinancialAccount> {
        val userId = sessionRepository.getUserId()
            ?: return NetworkResult.Error("User ID not found in session")

        return safeCall {
            val request = UpdateFinancialAccountRequestDto(
                name = name,
                isActive = isActive
            )

            val financialAccount = financialAccountFacadeService
                .updateFinancialAccount(
                    userId = userId,
                    accountId = accountId,
                    request = request
                )
                .toDomain()

            financialAccountDao.insert(financialAccount.toEntity(userId))
            financialAccount
        }
    }
}
