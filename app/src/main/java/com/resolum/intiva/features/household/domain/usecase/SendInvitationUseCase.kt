package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class SendInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(familyId: Long, userInvitedId: Long?): NetworkResult<Invitation> {
        if (familyId <= 0L) {
            return NetworkResult.Error("Family ID must be greater than zero")
        }
        if (userInvitedId != null && userInvitedId <= 0L) {
            return NetworkResult.Error("User invited ID must be greater than zero")
        }
        return invitationRepository.sendInvitation(familyId, userInvitedId)
    }
}
