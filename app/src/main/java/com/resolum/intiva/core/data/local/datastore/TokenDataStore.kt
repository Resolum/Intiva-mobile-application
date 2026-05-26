package com.resolum.intiva.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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
    /** Key used to store the authentication token and UserId in DataStore preferences. */
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val USER_ID = longPreferencesKey("user_id")
    }

    /** Flow that emits the current authentication token, or null if not set. */
    val authToken: Flow<String?> =
        dataStore.data.map { it[AUTH_TOKEN] }

    /** Flow that emits the current UserId, or null if not set. */
    val userId: Flow<Long?> =
        dataStore.data.map { it[USER_ID] }

    /** Saves the provided authentication token to DataStore preferences. */
    suspend fun saveToken(token: String, userId: Long) {
        dataStore.edit {
            it[AUTH_TOKEN] = token
            it[USER_ID] = userId
        }
    }

    /** Clears the authentication token from DataStore preferences. */
    suspend fun clearToken() {
        dataStore.edit {
            it.remove(AUTH_TOKEN)
            it.remove(USER_ID)
        }
    }
}