package com.resolum.intiva.features.iam.domain.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing onboarding state, such as whether the user has seen the onboarding screens.
 *
 * This interface abstracts the underlying data storage mechanism, allowing for
 * flexibility in how onboarding state is stored and retrieved (e.g., using DataStore,
 * SharedPreferences, or a database).
 */
interface OnboardingRepository {

    /**
     * A Flow that emits a boolean indicating whether the user has seen the onboarding screens.
     * This allows consumers to reactively observe changes to the onboarding state.
     */
    val hasSeenOnboarding: Flow<Boolean>

    /**
     * Marks the onboarding as shown, indicating that the user has seen the onboarding screens.
     */
    suspend fun setOnboardingShown()
}