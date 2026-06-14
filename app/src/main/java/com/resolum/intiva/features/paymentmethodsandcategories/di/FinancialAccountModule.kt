package com.resolum.intiva.features.paymentmethodsandcategories.di

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.FinancialAccountService
import com.resolum.intiva.features.paymentmethodsandcategories.data.repositories.FinancialAccountRepositoryImpl
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.FinancialAccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to financial accounts in the payment methods and categories feature.
 *
 * This module includes:
 * - Provision of the [FinancialAccountService] for making API calls related to financial accounts.
 * - Provision of the [FinancialAccountRepository] which is implemented by [FinancialAccountRepositoryImpl] for handling
 *   business logic and data operations related to financial accounts.
 *
 * The dependencies provided in this module are scoped to the singleton component, meaning they will
 * be shared across the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object FinancialAccountModule {

    /**
     * Provides an instance of [FinancialAccountService] using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of [FinancialAccountService] for making API calls related to financial accounts.
     */
    @Provides
    @Singleton
    fun provideFinancialAccountService(retrofit: Retrofit): FinancialAccountService = retrofit.create(
        FinancialAccountService::class.java)

    /**
     * Provides an instance of [FinancialAccountRepository] by injecting the [FinancialAccountRepositoryImpl].
     *
     * @param impl The implementation of [FinancialAccountRepository] to be provided.
     * @return An instance of [FinancialAccountRepository] for handling financial account-related data operations.
     */
    @Provides
    @Singleton
    fun provideFinancialAccountRepository(impl: FinancialAccountRepositoryImpl): FinancialAccountRepository = impl
}