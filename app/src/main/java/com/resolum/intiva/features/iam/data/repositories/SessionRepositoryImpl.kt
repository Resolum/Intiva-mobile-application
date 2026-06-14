package com.resolum.intiva.features.iam.data.repositories

import com.resolum.intiva.core.data.local.datastore.TokenDataStore
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [SessionRepository] that uses [TokenDataStore] to manage the authentication token.
 *
    * This repository provides a simple interface to access the current authentication token and to save or clear it.
 */
@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dataStore: TokenDataStore
) : SessionRepository {

    /**
     * The authentication token is exposed as a Flow from the data store, allowing
     * consumers to react to changes in the token (e.g., login/logout events).
     */
    override val authToken = dataStore.authToken

    /**
     * The saveToken and clearToken functions delegate to the data store, which handles
     * the actual persistence of the token. This keeps the repository focused on
     * business logic rather than data management details.
     */
    override suspend fun saveToken(token: String, userId: Long) = dataStore.saveToken(token, userId)

    /**
     * Clearing the token effectively logs the user out, and any consumers of [authToken]
     * will be notified of this change, allowing the UI to update accordingly.
     */
    override suspend fun clearToken() = dataStore.clearToken()

    /**
     * The getAuthToken and getUserId functions provide a way to retrieve the current token and user ID
     * synchronously, which can be useful in certain scenarios (e.g., when initializing network clients).
     */
    override suspend fun getAuthToken(): String? = dataStore.authToken.first()

    /**
     * The getUserId function retrieves the current user ID from the data store, allowing other parts of the application
     * to access this information when needed (e.g., for making authenticated API calls).
     */
    override suspend fun getUserId(): Long? = dataStore.userId.first()

    /**
     * Retrieves the cached family group ID from the data store.
     */
    override suspend fun getGroupId(): Long? = dataStore.groupId.first()

    /**
     * Persists the family group ID to the data store for future use.
     */
    override suspend fun saveGroupId(groupId: Long) = dataStore.saveGroupId(groupId)

}