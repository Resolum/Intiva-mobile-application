package com.resolum.intiva.features.communications.di

import com.resolum.intiva.features.communications.data.remote.services.NotificationDeviceService
import com.resolum.intiva.features.communications.data.repositories.NotificationDeviceRepositoryImpl
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationDeviceModule {

    @Provides
    @Singleton
    fun provideNotificationDeviceService(retrofit: Retrofit): NotificationDeviceService =
        retrofit.create(NotificationDeviceService::class.java)

    @Provides
    @Singleton
    fun provideNotificationDeviceRepository(
        impl: NotificationDeviceRepositoryImpl
    ): NotificationDeviceRepository = impl
}
