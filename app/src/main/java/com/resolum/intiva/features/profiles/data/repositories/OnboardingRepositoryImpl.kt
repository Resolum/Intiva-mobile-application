package com.resolum.intiva.features.profiles.data.repositories

import com.resolum.intiva.core.data.local.datastore.OnboardingDataStore
import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.core.network.model.map
import com.resolum.intiva.features.iam.data.repositories.SessionRepositoryImpl
import com.resolum.intiva.features.profiles.data.remote.OnboardingFacadeService
import com.resolum.intiva.features.profiles.data.remote.mappers.toDomain
import com.resolum.intiva.features.profiles.data.remote.mappers.toDto
import com.resolum.intiva.features.profiles.domain.models.AdvanceOnboardingRequest
import com.resolum.intiva.features.profiles.domain.models.OnboardingStatus
import com.resolum.intiva.features.profiles.domain.repositories.OnboardingRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * Implementation of [OnboardingRepository] that uses [OnboardingDataStore] to persist onboarding state.
 *
 * This repository provides a simple interface to check if the user has seen the onboarding screens and to mark the onboarding as shown.
 */
class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: OnboardingDataStore,
    private val sessionRepositoryImpl: SessionRepositoryImpl,
    private val onboardingFacadeService: OnboardingFacadeService
) : BaseRepository(), OnboardingRepository {

    /** Onboarding State Management */
    override val hasSeenOnboarding = dataStore.hasSeenOnboarding

    /** Sets the onboarding as shown in the data store. */
    override suspend fun setOnboardingShown() = dataStore.setOnboardingShown()

    /** Fetches the onboarding status for the current user. */
    override suspend fun getOnboardingStatus(): NetworkResult<OnboardingStatus> =
        safeCall {
            val userId = sessionRepositoryImpl.getUserId()
                ?: throw IllegalStateException("User ID not found in session")
            onboardingFacadeService.getOnboardingStatus(userId)
        }.map {
            it.toDomain()
        }

    /** Advances the onboarding step for the current user. */
    override suspend fun advanceOnboardingStep() {
        safeCall {
            val userId = sessionRepositoryImpl.getUserId()
                ?: throw IllegalStateException("User ID not found in session")
            onboardingFacadeService.advanceOnboardingStep(AdvanceOnboardingRequest(userId).toDto())
        }
    }

    /** Skips the onboarding process for the current user. */
    override suspend fun skipOnboarding() {
        safeCall {
            val userId = sessionRepositoryImpl.getUserId()
                ?: throw IllegalStateException("User ID not found in session")
            onboardingFacadeService.skipOnboarding(AdvanceOnboardingRequest(userId).toDto())
        }
    }

    /** Rolls back the onboarding step for the current user. */
    override suspend fun rollbackOnboardingStep() {
        safeCall {
            val userId = sessionRepositoryImpl.getUserId()
                ?: throw IllegalStateException("User ID not found in session")
            onboardingFacadeService.rollbackOnboardingStep(AdvanceOnboardingRequest(userId).toDto())
        }
    }
}
