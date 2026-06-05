package com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

/**
 * Repository interface for managing categories.
 */
interface CategoryRepository {


    suspend fun createCategory(
        name: String,
        description: String,
        color: String,
        icon: String
    ): NetworkResult<Category>

    suspend fun getCategoriesByOwnerId(
        ownerType: String,
        type: String
    ): NetworkResult<List<Category>>
}