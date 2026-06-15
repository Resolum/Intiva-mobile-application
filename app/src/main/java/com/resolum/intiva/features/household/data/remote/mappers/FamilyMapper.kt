package com.resolum.intiva.features.household.data.remote.mappers

import com.resolum.intiva.features.household.data.remote.models.CreateFamilyRequestDto
import com.resolum.intiva.features.household.data.remote.models.FamilyResponseDto
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.models.Family

fun FamilyResponseDto.toDomain(): Family = Family(
    id = id,
    name = name,
    description = description,
    status = status,
    ownerId = ownerId,
    membersActive = membersActive,
    createdAt = createdAt
)

fun CreateFamilyRequest.toDto(): CreateFamilyRequestDto = CreateFamilyRequestDto(
    name = name,
    description = description
)
