package com.resolum.intiva.features.profiles.di

import com.resolum.intiva.features.profiles.data.remote.services.ProfileService
import com.resolum.intiva.features.profiles.data.repositories.ProfileRepositoryImpl
import com.resolum.intiva.features.profiles.domain.repositories.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl
}
