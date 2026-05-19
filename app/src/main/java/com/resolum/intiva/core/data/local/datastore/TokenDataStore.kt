package com.resolum.intiva.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Local data source that persists and retrieves the authentication token using DataStore.
 */
@Singleton
class TokenDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    /** Key used to store the authentication token in DataStore preferences. */
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    /** Flow that emits the current authentication token, or null if not set. */
    val authToken: Flow<String?> =
        dataStore.data.map { it[AUTH_TOKEN] }

    /** Saves the provided authentication token to DataStore preferences. */
    suspend fun saveToken(token: String) {
        dataStore.edit { it[AUTH_TOKEN] = token }
    }

    /** Clears the authentication token from DataStore preferences. */
    suspend fun clearToken() {
        dataStore.edit { it.remove(AUTH_TOKEN) }
    }
}