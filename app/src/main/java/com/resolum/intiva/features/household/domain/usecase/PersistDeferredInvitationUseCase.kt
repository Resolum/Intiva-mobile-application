package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class PersistDeferredInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(installId: String, token: String): NetworkResult<Unit> {
        if (installId.isBlank()) {
            return NetworkResult.Error("Install ID is required")
        }
        if (token.isBlank()) {
            return NetworkResult.Error("Token is required")
        }
        return invitationRepository.persistDeferredInvitation(installId, token)
    }
}
