package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class SendLinkInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(familyId: Long): NetworkResult<String> {
        if (familyId <= 0L) {
            return NetworkResult.Error("Family ID must be greater than zero")
        }
        return invitationRepository.sendInvitationLink(familyId)
    }
}
