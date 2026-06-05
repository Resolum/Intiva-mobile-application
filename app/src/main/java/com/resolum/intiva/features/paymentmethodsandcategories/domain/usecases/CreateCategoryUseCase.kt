package com.resolum.intiva.features.paymentmethodsandcategories.domain.usecases

import com.resolum.intiva.features.paymentmethodsandcategories.domain.repositories.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        color: String,
        icon: String
    ) = categoryRepository.createCategory(
        name = name,
        description = description,
        color = color,
        icon = icon
    )
}