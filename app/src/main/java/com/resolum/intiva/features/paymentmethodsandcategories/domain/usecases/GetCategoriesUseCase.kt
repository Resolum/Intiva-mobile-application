package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category
import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Use case for fetching categories associated with the user. This use case interacts with the CategoryRepository
 * to retrieve the list of categories based on the user's ID.
 *
 * The use case is designed to be invoked from the presentation layer (e.g., ViewModel) to fetch categories and handle
 * the result, which can be a success with a list of categories or an error with an appropriate message and throwable.
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Category>> =
        repository.getCategoriesByUserId()
}
