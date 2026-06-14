package com.resolum.intiva.features.paymentmethodsandcategories.data.local.mappers

import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.CategoryEntity
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CategoryResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.domain.models.Category

fun CategoryResponseDto.toEntity(type: String): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        ownerType = ownerType,
        ownerId = ownerId,
        groupId = groupId,
        type = type,
        description = description,
        color = color,
        icon = icon,
        isActive = isActive
    )
}

fun Category.toEntity(type: String): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        ownerType = ownerType,
        ownerId = ownerId,
        groupId = groupId,
        type = type,
        description = description,
        color = color,
        icon = icon,
        isActive = isActive
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        ownerType = ownerType,
        ownerId = ownerId,
        groupId = groupId,
        isActive = isActive,
        description = description,
        color = color,
        icon = icon
    )
}

fun List<CategoryResponseDto>.toEntities(type: String): List<CategoryEntity> {
    return map { it.toEntity(type) }
}

fun List<CategoryEntity>.toDomainCategories(): List<Category> {
    return map { it.toDomain() }
}
