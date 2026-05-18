package com.resolum.intiva.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "intiva_auth")

/**
 * Local data source that persists and retrieves the authentication token using DataStore.
 */
@Singleton
class TokenDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    /** Key used to store the authentication token in DataStore preferences. */
    private val tokenKey = stringPreferencesKey("auth_token")

    /** Flow that emits the current authentication token, or null if not set. */
    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[tokenKey]
    }

    /** Saves the provided authentication token to DataStore. */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    /** Clears the authentication token from DataStore, effectively logging the user out. */
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }
}