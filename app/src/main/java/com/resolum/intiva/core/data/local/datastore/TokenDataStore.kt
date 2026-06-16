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
    /** Key used to store the authentication token, UserId, and GroupId in DataStore preferences. */
    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val USER_ID    = longPreferencesKey("user_id")
        private val GROUP_ID   = longPreferencesKey("group_id")
    }

    /** Flow that emits the current authentication token, or null if not set. */
    val authToken: Flow<String?> =
        dataStore.data.map { it[AUTH_TOKEN] }

    /** Flow that emits the current UserId, or null if not set. */
    val userId: Flow<Long?> =
        dataStore.data.map { it[USER_ID] }

    /** Flow that emits the current family GroupId, or null if not set. */
    val groupId: Flow<Long?> =
        dataStore.data.map { it[GROUP_ID] }

    /** Saves the provided authentication token to DataStore preferences.
     * If the userId differs from the stored one, the cached groupId is cleared
     * so a different user won't use a stale group from the previous session. */
    suspend fun saveToken(token: String, userId: Long) {
        dataStore.edit { prefs ->
            val previousUserId = prefs[USER_ID]
            prefs[AUTH_TOKEN] = token
            prefs[USER_ID] = userId
            if (previousUserId != null && previousUserId != userId) {
                prefs.remove(GROUP_ID)
            }
        }
    }

    /** Saves the family group ID to DataStore preferences. */
    suspend fun saveGroupId(groupId: Long) {
        dataStore.edit { it[GROUP_ID] = groupId }
    }

    /** Clears the authentication token from DataStore preferences. */
    suspend fun clearToken() {
        dataStore.edit {
            it.remove(AUTH_TOKEN)
            it.remove(USER_ID)
        }
    }
}