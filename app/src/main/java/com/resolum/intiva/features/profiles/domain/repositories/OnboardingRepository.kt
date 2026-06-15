package com.resolum.intiva.features.profiles.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.domain.models.OnboardingStatus
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

    /**
     * Retrieves the current onboarding status for the user.
     *
     * @return A [NetworkResult] containing either a successful [OnboardingStatus] or an error message.
     */
    suspend fun getOnboardingStatus(): NetworkResult<OnboardingStatus>

    /**
     * Advances the onboarding step for the user, moving them to the next step in the onboarding process.
     */
    suspend fun advanceOnboardingStep()

    /**
     * Skips the onboarding process for the user.
     */
    suspend fun skipOnboarding()

    /**
     * Rolls back the onboarding step for the user, moving them back to the previous step in the onboarding process.
     */
    suspend fun rollbackOnboardingStep()
}
