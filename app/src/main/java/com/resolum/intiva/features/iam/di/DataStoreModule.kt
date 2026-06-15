package com.resolum.intiva.features.iam.di

import com.resolum.intiva.features.iam.data.repositories.SessionRepositoryImpl
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Hilt module for providing data store repositories related to the IAM feature.
 *
 * This module binds the concrete implementation of [SessionRepository] to its interface,
 * allowing it to be injected wherever needed in the application.
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
}
