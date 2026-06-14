package com.resolum.intiva.features.paymentmethodsandcategories.data.remote

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateCategoryRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.CategoryService
import javax.inject.Inject

/**
 * Facade service for category-related operations.
 */
class CategoryFacadeService @Inject constructor(
    private val categoryService: CategoryService
) {

    suspend fun getCategoriesByOwnerId(
        ownerType: String,
        ownerId: Long,
        type: String? = null
    ) = categoryService.getCategoriesByOwnerId(
        ownerType = ownerType,
        ownerId = ownerId,
        type = type
    )

    suspend fun createCategory(
        request: CreateCategoryRequestDto
    ) = categoryService.createCategory(
        request = request
    )
}