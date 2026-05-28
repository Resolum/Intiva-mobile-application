package com.resolum.intiva.features.finances.di

import com.resolum.intiva.features.finances.data.remote.services.TransactionService
import com.resolum.intiva.features.finances.data.repositories.TransactionRepositoryImpl
import com.resolum.intiva.features.finances.domain.repositories.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to financial transactions in the finances feature.
 *
 * This module includes:
 * - Provision of the [TransactionService] for making API calls related to financial transactions.
 * - Provision of the [TransactionRepository] which is implemented by [TransactionRepositoryImpl] for handling
 *   business logic and data operations related to financial transactions.
 *
 * The dependencies provided in this module are scoped to the singleton component, meaning they will
 * be shared across the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    /**
     * Provides an instance of [TransactionService] using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of [TransactionService] for making API calls related to transactions.
     */
    @Provides
    @Singleton
    fun provideTransactionService(retrofit: Retrofit) : TransactionService = retrofit.create(
        TransactionService::class.java
    )

    /**
     * Provides an instance of [TransactionRepository] by injecting the [TransactionRepositoryImpl].
     *
     * @param impl The implementation of [TransactionRepository] to be provided.
     * @return An instance of [TransactionRepository] for handling transaction-related data operations.
     */
    @Provides
    @Singleton
    fun provideTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository = impl
}