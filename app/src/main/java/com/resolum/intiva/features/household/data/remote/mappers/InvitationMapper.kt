package com.resolum.intiva.features.household.data.remote.mappers

import com.resolum.intiva.features.household.data.remote.models.InvitationResponseDto
import com.resolum.intiva.features.household.domain.models.Invitation

fun InvitationResponseDto.toDomain(): Invitation = Invitation(
    id = id,
    token = token,
    status = status,
    sentAt = sentAt,
    expiresAt = expiresAt,
    respondedAt = respondedAt,
    invitedBy = invitedBy,
    familyId = familyId,
    userInvitedId = userInvitedId,
    isExpired = isExpired
)
