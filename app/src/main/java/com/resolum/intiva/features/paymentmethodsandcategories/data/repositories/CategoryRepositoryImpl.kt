package com.resolum.intiva.features.paymentmethodsandcategories.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.finances.domain.models.TransactionType
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.CategoryFacadeService
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.mappers.toDomain
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateCategoryRequestDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services.CategoryService
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import com.resolum.intiva.features.shared.domain.model.OwnerType
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

    override suspend fun getCategoriesByOwnerId(
        ownerType: String,
        type: String
    ): NetworkResult<List<Category>> =
        safeCall {
            val userId = sessionRepository.getUserId()
                ?: throw IllegalStateException("No active session found")

            categoryFacadeService.getCategoriesByOwnerId(
                ownerType = ownerType,
                ownerId = userId,
                type = type
            ).map { it.toDomain() }
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
                ownerType = OwnerType.INDIVIDUAL.name,
                ownerId = userId,
                description = description,
                color = color,
                icon = icon,
                type = TransactionType.EXPENSE.name
            )

            categoryFacadeService.createCategory(request).toDomain()
        }
}