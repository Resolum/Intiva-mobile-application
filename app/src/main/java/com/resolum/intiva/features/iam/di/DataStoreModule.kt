package com.resolum.intiva.features.iam.di

import com.resolum.intiva.features.iam.data.repositories.OnboardingRepositoryImpl
import com.resolum.intiva.features.iam.data.repositories.SessionRepositoryImpl
import com.resolum.intiva.features.iam.domain.repositories.OnboardingRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Hilt module for providing data store repositories related to the IAM feature.
 *
 * This module binds the concrete implementations of [SessionRepository] and
 * [OnboardingRepository] to their respective interfaces, allowing them to be
 * injected wherever needed in the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /** Provides the implementation of SessionRepository.
     *
     * @param impl The concrete implementation of SessionRepository, provided by Hilt.
     * @return The SessionRepository interface, backed by the provided implementation.
     */
    @Provides
    @Singleton
    fun provideSessionRepository(impl: SessionRepositoryImpl): SessionRepository = impl

    /** Provides the implementation of OnboardingRepository.
     *
     * @param impl The concrete implementation of OnboardingRepository, provided by Hilt.
     * @return The OnboardingRepository interface, backed by the provided implementation.
     */
    @Provides
    @Singleton
    fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository = impl
}