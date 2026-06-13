package com.resolum.intiva.features.household.di

import com.resolum.intiva.features.household.data.remote.FamilyFacadeService
import com.resolum.intiva.features.household.data.remote.services.FamilyMemberService
import com.resolum.intiva.features.household.data.remote.services.FamilyService
import com.resolum.intiva.features.household.data.remote.services.InvitationService
import com.resolum.intiva.features.household.data.repositories.FamilyMemberRepositoryImpl
import com.resolum.intiva.features.household.data.repositories.FamilyRepositoryImpl
import com.resolum.intiva.features.household.data.repositories.InvitationRepositoryImpl
import com.resolum.intiva.features.household.domain.repositories.FamilyMemberRepository
import com.resolum.intiva.features.household.domain.repositories.FamilyRepository
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HouseholdModule {

    @Provides
    @Singleton
    fun provideFamilyService(retrofit: Retrofit): FamilyService =
        retrofit.create(FamilyService::class.java)

    @Provides
    @Singleton
    fun provideFamilyMemberService(retrofit: Retrofit): FamilyMemberService =
        retrofit.create(FamilyMemberService::class.java)

    @Provides
    @Singleton
    fun provideInvitationService(retrofit: Retrofit): InvitationService =
        retrofit.create(InvitationService::class.java)

    @Provides
    @Singleton
    fun provideFamilyRepository(impl: FamilyRepositoryImpl): FamilyRepository = impl

    @Provides
    @Singleton
    fun provideFamilyMemberRepository(impl: FamilyMemberRepositoryImpl): FamilyMemberRepository = impl

    @Provides
    @Singleton
    fun provideInvitationRepository(impl: InvitationRepositoryImpl): InvitationRepository = impl
}
