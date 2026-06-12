package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.InvitationDetail
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import jakarta.inject.Inject

class GetInvitationByTokenUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    suspend operator fun invoke(token: String): NetworkResult<InvitationDetail> {
        if (token.isBlank()) {
            return NetworkResult.Error("Token is required")
        }

        val result = invitationRepository.getPendingInvitations()

        return when (result) {
            is NetworkResult.Success -> {
                val invitation = result.data.find { it.token == token }
                if (invitation != null) {
                    if (invitation.isExpired) {
                        NetworkResult.Error("Esta invitación ha expirado")
                    } else {
                        NetworkResult.Success(
                            InvitationDetail(
                                id = invitation.id,
                                token = invitation.token,
                                status = invitation.status,
                                invitedByName = "Usuario #${invitation.invitedBy}",
                                familyId = invitation.familyId,
                                isExpired = invitation.isExpired
                            )
                        )
                    }
                } else {
                    NetworkResult.Error("Invitación no encontrada")
                }
            }
            is NetworkResult.Error -> result
        }
    }
}
