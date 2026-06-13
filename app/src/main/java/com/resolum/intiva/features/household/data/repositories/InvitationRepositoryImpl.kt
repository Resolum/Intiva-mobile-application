package com.resolum.intiva.features.household.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.data.remote.FamilyFacadeService
import com.resolum.intiva.features.household.data.remote.mappers.toDomain
import com.resolum.intiva.features.household.data.remote.models.SendInvitationRequestDto
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.models.QrCodeResult
import com.resolum.intiva.features.household.domain.repositories.InvitationRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

class InvitationRepositoryImpl @Inject constructor(
    private val familyFacadeService: FamilyFacadeService,
    private val sessionRepository: SessionRepository
) : BaseRepository(), InvitationRepository {

    override suspend fun sendInvitation(familyId: Long, userInvitedId: Long?): NetworkResult<Invitation> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.sendInvitation(userId, familyId, SendInvitationRequestDto(userInvitedId)).toDomain()
    }

    override suspend fun getInvitations(): NetworkResult<List<Invitation>> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.getInvitations(userId).map { it.toDomain() }
    }

    override suspend fun getPendingInvitations(): NetworkResult<List<Invitation>> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.getPendingInvitations(userId).map { it.toDomain() }
    }

    override suspend fun acceptInvitation(invitationId: Long): NetworkResult<Invitation> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.acceptInvitation(userId, invitationId).toDomain()
    }

    override suspend fun rejectInvitation(invitationId: Long): NetworkResult<Invitation> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.rejectInvitation(userId, invitationId).toDomain()
    }

    override suspend fun getDeferredInvitation(installId: String): NetworkResult<DeepLinkData> = safeCall {
        val dto = familyFacadeService.getDeferredInvitation(installId)
        DeepLinkData(
            token = dto.token,
            groupName = dto.groupName ?: "",
            inviterName = dto.inviterName ?: ""
        )
    }

    override suspend fun acceptInvitationByToken(token: String): NetworkResult<Unit> = safeCall {
        familyFacadeService.acceptInvitationByToken(token)
        Unit
    }

    override suspend fun rejectInvitationByToken(token: String): NetworkResult<String> = safeCall {
        familyFacadeService.rejectInvitationByToken(token).invitedByName
    }

    override suspend fun getInvitationQr(familyId: Long): NetworkResult<QrCodeResult> = safeCall {
        val dto = familyFacadeService.getInvitationQr(familyId)
        QrCodeResult(
            qrBase64 = dto.qrBase64,
            invitationLink = dto.invitationLink,
            token = dto.token,
            expiresAt = dto.expiresAt
        )
    }
}
