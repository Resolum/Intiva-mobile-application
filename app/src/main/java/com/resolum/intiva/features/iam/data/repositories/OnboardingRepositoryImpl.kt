package com.resolum.intiva.features.iam.data.repositories

import com.resolum.intiva.core.data.local.datastore.OnboardingDataStore
import com.resolum.intiva.features.iam.domain.repositories.OnboardingRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * Implementation of [OnboardingRepository] that uses [OnboardingDataStore] to persist onboarding state.
 *
    * This repository provides a simple interface to check if the user has seen the onboarding screens and to mark the onboarding as shown.
 */
@Singleton
class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: OnboardingDataStore,
) : OnboardingRepository {

    /** Onboarding State Management */
    override val hasSeenOnboarding = dataStore.hasSeenOnboarding

    /** Sets the onboarding as shown in the data store. */
    override suspend fun setOnboardingShown() = dataStore.setOnboardingShown()
}