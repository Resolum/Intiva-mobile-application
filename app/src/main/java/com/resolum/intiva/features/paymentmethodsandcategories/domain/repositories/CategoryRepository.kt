package com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

/**
 * Repository interface for managing categories, providing methods to fetch categories associated with the user.
 *
 * This interface abstracts the data source for categories, allowing for different implementations (e.g., remote API, local database)
 * without affecting the rest of the application. It defines a method to retrieve categories based on the user's ID.
 */
interface CategoryRepository {

    suspend fun getCategoriesByOwnerId(ownerType: String, type: String): NetworkResult<List<Category>>
}