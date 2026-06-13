package com.resolum.intiva.features.household.data.remote.mappers

import com.resolum.intiva.features.household.data.remote.models.FamilyMemberResponseDto
import com.resolum.intiva.features.household.domain.models.FamilyMember

fun FamilyMemberResponseDto.toDomain(): FamilyMember = FamilyMember(
    id = id,
    userId = userId,
    familyId = familyId,
    role = role,
    status = status,
    joinedAt = joinedAt
)
