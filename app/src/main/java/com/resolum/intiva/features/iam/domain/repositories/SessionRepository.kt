package com.resolum.intiva.features.iam.domain.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing user session data, such as authentication tokens.
 *
 * This interface abstracts the underlying data storage mechanism, allowing for
 * flexibility in how session data is stored and retrieved (e.g., using DataStore,
 * SharedPreferences, or a database).
 */
interface SessionRepository {

    /**
     * A Flow that emits the current authentication token, or null if no token is stored.
     * This allows consumers to reactively observe changes to the authentication state.
     */
    val authToken: Flow<String?>

    /**
     * Saves the provided authentication token to the data store.
     *
     * @param token The authentication token to be saved.
     */
    suspend fun saveToken(token: String)

    /**
     * Clears the stored authentication token, effectively logging the user out.
     */
    suspend fun clearToken()
}