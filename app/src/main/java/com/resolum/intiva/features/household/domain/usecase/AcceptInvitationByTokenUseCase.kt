package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class AcceptInvitationByTokenUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(token: String): NetworkResult<Unit> {
        if (token.isBlank()) {
            return NetworkResult.Error("Token is required")
        }
        return invitationRepository.acceptInvitationByToken(token)
    }
}
