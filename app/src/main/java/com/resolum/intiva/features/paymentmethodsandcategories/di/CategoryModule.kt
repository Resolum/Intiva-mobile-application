package com.resolum.intiva.features.paymentmethodsandcategories.di

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.CategoryService
import com.resolum.intiva.features.paymentmethodsandcategories.data.repositories.CategoryRepositoryImpl
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to categories in the payment methods and categories feature.
 *
 * This module includes:
 * - Provision of the [CategoryService] for making API calls related to categories.
 * - Provision of the [CategoryRepository] which is implemented by [CategoryRepositoryImpl] for handling
 *   business logic and data operations related to categories.
 *
 * The dependencies provided in this module are scoped to the singleton component, meaning they will
 * be shared across the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {

    /**
     * Provides an instance of [CategoryService] using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of [CategoryService] for making API calls related to categories.
     */
    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit) : CategoryService = retrofit.create(
        CategoryService::class.java)

    /**
     * Provides an instance of [CategoryRepository] by injecting the [CategoryRepositoryImpl].
     *
     * @param impl The implementation of [CategoryRepository] to be provided.
     * @return An instance of [CategoryRepository] for handling category-related data operations.
     */
    @Provides
    @Singleton
    fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository = impl
}