package com.resolum.intiva.features.savings.di

import com.resolum.intiva.features.savings.data.remote.services.SavingGoalService
import com.resolum.intiva.features.savings.data.repositories.SavingGoalRepositoryImpl
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to the savings feature.
 *
 * Scoped to the [SingletonComponent] to ensure that singleton instances of our services
 * and repositories are shared throughout the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object SavingsModule {

    /**
     * Provides the Retrofit [SavingGoalService] instance.
     *
     * Injects the single, authenticated [Retrofit] instance (configured in NetworkModule
     * with the AuthInterceptor) to handle all savings-related API requests.
     *
     * @param retrofit The authenticated Retrofit builder instance.
     * @return The Retrofit implementation of [SavingGoalService].
     */
    @Provides
    @Singleton
    fun provideSavingGoalService(retrofit: Retrofit): SavingGoalService = 
        retrofit.create(SavingGoalService::class.java)

    /**
     * Provides the [SavingGoalRepository] implementation.
     *
     * @param impl The implementation class [SavingGoalRepositoryImpl].
     * @return An instance of [SavingGoalRepository].
     */
    @Provides
    @Singleton
    fun provideSavingGoalRepository(impl: SavingGoalRepositoryImpl): SavingGoalRepository = impl
}
