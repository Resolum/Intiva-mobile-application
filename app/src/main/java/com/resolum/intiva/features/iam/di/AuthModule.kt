package com.resolum.intiva.features.iam.di

import com.resolum.intiva.features.iam.domain.repositories.AuthRepository
import com.resolum.intiva.features.iam.data.repositories.AuthRepositoryImpl
import com.resolum.intiva.features.iam.data.remote.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * AuthModule is a Dagger Hilt module that provides dependencies related to authentication.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    /**
     * Provides an instance of AuthService using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the AuthService.
     * @return An instance of AuthService.
     */
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    /**
     * Provides an instance of AuthRepository.
     *
     * @param impl The implementation of AuthRepository (AuthRepositoryImpl).
     * @return An instance of AuthRepository.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}