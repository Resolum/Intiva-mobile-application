package com.resolum.intiva.features.communications.di

import com.resolum.intiva.features.communications.data.remote.services.InAppNotificationService
import com.resolum.intiva.features.communications.data.repositories.InAppNotificationRepositoryImpl
import com.resolum.intiva.features.communications.domain.repositories.InAppNotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InAppNotificationModule {

    @Provides
    @Singleton
    fun provideInAppNotificationService(retrofit: Retrofit): InAppNotificationService =
        retrofit.create(InAppNotificationService::class.java)

    @Provides
    @Singleton
    fun provideInAppNotificationRepository(
        impl: InAppNotificationRepositoryImpl
    ): InAppNotificationRepository = impl
}
