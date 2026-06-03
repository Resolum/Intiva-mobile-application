package com.resolum.intiva.features.finances.di

import com.resolum.intiva.features.finances.data.remote.services.SpendingLimitService
import com.resolum.intiva.features.finances.data.repositories.SpendingLimitRepositoryImpl
import com.resolum.intiva.features.finances.domain.repositories.SpendingLimitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpendingLimitsModule {

    @Provides
    @Singleton
    fun provideSpendingLimitsService(retrofit: Retrofit) : SpendingLimitService = retrofit.create(
        SpendingLimitService::class.java
    )

    @Provides
    @Singleton
    fun provideSpendingLimitRepository(
        impl: SpendingLimitRepositoryImpl
    ): SpendingLimitRepository = impl

}