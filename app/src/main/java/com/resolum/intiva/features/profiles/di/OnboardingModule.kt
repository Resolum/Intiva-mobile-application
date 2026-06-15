package com.resolum.intiva.features.profiles.di

import com.resolum.intiva.features.profiles.data.remote.services.OnboardingService
import com.resolum.intiva.features.profiles.data.repositories.OnboardingRepositoryImpl
import com.resolum.intiva.features.profiles.domain.repositories.OnboardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * OnboardingModule is a Dagger Hilt module that provides dependencies related to the onboarding feature.
 *
 * This module includes provisions for the OnboardingService, which handles remote API calls related to onboarding,
 * and the OnboardingRepository, which manages the onboarding state and interactions with the data store.
 */
@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    /**
     * Provides an instance of OnboardingService using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the OnboardingService.
     * @return An instance of OnboardingService.
     */
    @Provides
    @Singleton
    fun provideOnboardingService(retrofit: Retrofit) : OnboardingService = retrofit.create(OnboardingService::class.java)

    @Provides
    @Singleton
    fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository = impl
}
