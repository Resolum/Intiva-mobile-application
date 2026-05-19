package com.resolum.intiva.features.iam.data.repositories

import com.resolum.intiva.core.data.local.datastore.TokenDataStore
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
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
    override suspend fun saveToken(token: String) = dataStore.saveToken(token)

    /**
     * Clearing the token effectively logs the user out, and any consumers of [authToken]
     * will be notified of this change, allowing the UI to update accordingly.
     */
    override suspend fun clearToken() = dataStore.clearToken()

}