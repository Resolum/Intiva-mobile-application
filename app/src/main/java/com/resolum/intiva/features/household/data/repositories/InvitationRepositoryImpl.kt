package com.resolum.intiva.features.household.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.data.remote.FamilyFacadeService
import com.resolum.intiva.features.household.data.remote.mappers.toDomain
import com.resolum.intiva.features.household.data.remote.models.PersistDeferredInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.SendInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.SendLinkInvitationRequestDto
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.models.InvitationDetail
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
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        val dto = familyFacadeService.getPublicInvitation(token)
        val invitationId = dto.id ?: throw IllegalStateException("Invitation ID not found in public response")
        familyFacadeService.acceptInvitation(userId, invitationId)
        Unit
    }

    override suspend fun rejectInvitationByToken(token: String): NetworkResult<String> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        val dto = familyFacadeService.getPublicInvitation(token)
        val invitationId = dto.id ?: throw IllegalStateException("Invitation ID not found in public response")
        familyFacadeService.rejectInvitation(userId, invitationId)
        "Invitación rechazada"
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

    override suspend fun sendInvitationLink(familyId: Long, inviteeEmail: String?): NetworkResult<String> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        val email = inviteeEmail ?: "link-${familyId}-${System.currentTimeMillis()}@invite.intiva"
        val response = familyFacadeService.sendLinkInvitation(
            userId,
            SendLinkInvitationRequestDto(familyId = familyId, inviteeEmail = email)
        )
        response.inviteUrl
    }

    override suspend fun persistDeferredInvitation(installId: String, token: String): NetworkResult<Unit> = safeCall {
        familyFacadeService.persistDeferredInvitation(
            PersistDeferredInvitationRequestDto(installId = installId, token = token)
        )
    }

    override suspend fun getPublicInvitation(token: String): NetworkResult<InvitationDetail> = safeCall {
        val dto = familyFacadeService.getPublicInvitation(token)
        InvitationDetail(
            id = dto.id ?: 0L,
            token = dto.token ?: token,
            status = dto.status ?: "PENDING",
            invitedByName = dto.invitedByName ?: "Usuario",
            familyId = dto.familyId ?: 0L,
            isExpired = dto.isExpired ?: false
        )
    }
}
