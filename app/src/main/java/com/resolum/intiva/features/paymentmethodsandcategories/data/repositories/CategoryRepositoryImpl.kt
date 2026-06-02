package com.resolum.intiva.features.paymentmethodsandcategories.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.CategoryFacadeService
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.mappers.toDomain
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateCategoryRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.CategoryService
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Implementation of the [CategoryRepository] interface that interacts with the [CategoryFacadeService]
 * to fetch category data from the remote API. It also uses the [SessionRepository] to retrieve
 * the current user's session information, such as the user ID, which is required for fetching
 * categories specific to the user.
 *
 * This repository handles the business logic for fetching categories and converting them into
 * domain models that can be used by the rest of the application. It also manages error handling
 * and ensures that network calls are made safely using the base repository's functionality.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val categoryFacadeService: CategoryFacadeService,
    private val sessionRepository: SessionRepository
) : BaseRepository(), CategoryRepository {

    /**
     * Fetches the list of categories associated with the current user. It retrieves the user ID
     * from the session repository and then calls the category facade service to get the categories.
     * The result is mapped to a list of domain models and returned as a [NetworkResult].
     *
     * @return A [NetworkResult] containing a list of [Category] objects on success, or an error message on failure.
     */
    override suspend fun getCategoriesByUserId(): NetworkResult<List<Category>> =
        safeCall {
            val userId = sessionRepository.getUserId() ?: throw IllegalStateException("No active session found")
            categoryFacadeService.getCategoriesByUserId(userId).map { it.toDomain() }
        }

    override suspend fun createCategory(
        name: String,
        description: String,
        color: String,
        icon: String
    ): NetworkResult<Category> =
        safeCall {
            val userId = sessionRepository.getUserId()
                ?: throw IllegalStateException("No active session found")

            val request = CreateCategoryRequestDto(
                name = name,
                ownerType = "USER",
                description = description,
                color = color,
                icon = icon,
                isActive = true
            )

            categoryFacadeService.createCategory(userId, request).toDomain()
        }
}