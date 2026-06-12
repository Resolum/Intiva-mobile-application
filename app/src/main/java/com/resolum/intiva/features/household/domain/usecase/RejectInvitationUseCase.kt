package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class RejectInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(invitationId: Long): NetworkResult<Invitation> {
        if (invitationId <= 0L) {
            return NetworkResult.Error("Invitation ID must be greater than zero")
        }
        return invitationRepository.rejectInvitation(invitationId)
    }
}
