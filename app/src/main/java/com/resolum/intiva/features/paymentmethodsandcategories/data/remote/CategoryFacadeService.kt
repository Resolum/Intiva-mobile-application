package com.resolum.intiva.features.paymentmethodsandcategories.data.remote

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.CategoryService
import javax.inject.Inject

/**
 * Facade service for category-related operations.
 *
 * This service abstracts the underlying category data retrieval mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles fetching categories based on user ID by
 * delegating to the appropriate services.
 */
class CategoryFacadeService @Inject constructor(
    private val categoryService: CategoryService
) {

    suspend fun getCategoriesByOwnerId(ownerType: String, ownerId: Long, type: String) = categoryService.getCategoriesByOwnerId(ownerType, ownerId, type)
}