package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class GetDeferredInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(installId: String): NetworkResult<DeepLinkData> {
        if (installId.isBlank()) {
            return NetworkResult.Error("Install ID is required")
        }
        return invitationRepository.getDeferredInvitation(installId)
    }
}
