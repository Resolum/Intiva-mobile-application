package com.resolum.intiva.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

/**
 * DataStore for onboarding-related preferences.
 *
 * This class provides a simple API to read and write onboarding state, such as whether the user has seen the onboarding screens.
 * The onboarding state is stored as a boolean preference, and the class exposes a Flow to observe changes to this state.
 */
@Singleton
class OnboardingDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    /**
     * Key for tracking if the user has seen the onboarding screens.
     */
    companion object {
        private val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    /**
     * Flow that emits whether the user has seen the onboarding screens.
     */
    val hasSeenOnboarding: Flow<Boolean> =
        dataStore.data.map { it[HAS_SEEN_ONBOARDING] ?: false }

    /**
     * Marks the onboarding as shown by setting the corresponding preference to true.
     */
    suspend fun setOnboardingShown() {
        dataStore.edit { it[HAS_SEEN_ONBOARDING] = true }
    }
}