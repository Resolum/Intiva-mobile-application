package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.mappers

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CategoryResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

/**
 * Extension function to map a [CategoryResponseDto] to a [Category] domain model.
 *
 * This function takes a [CategoryResponseDto] object and converts it into a [Category] object
 * by mapping each corresponding field. This allows the application to work with the domain model
 * while still being able to receive data from the remote API in the form of DTOs.
 */
fun CategoryResponseDto.toDomain() = Category(
    id = id,
    name = name,
    ownerType = ownerType,
    userId = userId,
    groupId = groupId,
    isActive = isActive,
    description = description,
    color = color,
    icon = icon
)