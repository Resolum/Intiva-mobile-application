package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.InvitationDetail
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class GetPublicInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(token: String): NetworkResult<InvitationDetail> {
        if (token.isBlank()) {
            return NetworkResult.Error("Token is required")
        }
        return invitationRepository.getPublicInvitation(token)
    }
}
