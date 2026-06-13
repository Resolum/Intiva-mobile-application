package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class GetInvitationsUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Invitation>> {
        return invitationRepository.getInvitations()
    }
}
