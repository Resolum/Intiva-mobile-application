package com.resolum.intiva.features.household.data.remote

import com.resolum.intiva.features.household.data.remote.models.AssignRoleRequestDto
import com.resolum.intiva.features.household.data.remote.models.CreateFamilyRequestDto
import com.resolum.intiva.features.household.data.remote.models.CreateQrInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.FamilyMemberResponseDto
import com.resolum.intiva.features.household.data.remote.models.FamilyResponseDto
import com.resolum.intiva.features.household.data.remote.models.InvitationResponseDto
import com.resolum.intiva.features.household.data.remote.models.QrCodeResponseDto
import com.resolum.intiva.features.household.data.remote.models.SendInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.services.FamilyMemberService
import com.resolum.intiva.features.household.data.remote.services.FamilyService
import com.resolum.intiva.features.household.data.remote.services.InvitationService
import javax.inject.Inject

class FamilyFacadeService @Inject constructor(
    private val familyService: FamilyService,
    private val familyMemberService: FamilyMemberService,
    private val invitationService: InvitationService
) {
    suspend fun createFamily(userId: Long, request: CreateFamilyRequestDto): FamilyResponseDto =
        familyService.createFamily(userId, request)

    suspend fun getFamilyById(userId: Long, id: Long): FamilyResponseDto =
        familyService.getFamilyById(userId, id)

    /** Unwraps the members wrapper object and returns the inner list. */
    suspend fun getFamilyMembers(userId: Long, familyId: Long): List<FamilyMemberResponseDto> =
        familyMemberService.getFamilyMembers(userId, familyId).members

    suspend fun getFamilyMemberById(userId: Long, familyId: Long, memberId: Long): FamilyMemberResponseDto =
        familyMemberService.getFamilyMemberById(userId, familyId, memberId)

    suspend fun assignRole(userId: Long, familyId: Long, memberId: Long, request: AssignRoleRequestDto): FamilyMemberResponseDto =
        familyMemberService.assignRole(userId, familyId, memberId, request)

    suspend fun sendInvitation(userId: Long, familyId: Long, request: SendInvitationRequestDto): InvitationResponseDto =
        invitationService.sendInvitation(userId, familyId, request)

    /**
     * Creates an open QR invitation without a specific target user.
     * Sends an empty body `{}` to avoid null constraint violations on the backend.
     */
    suspend fun sendQrInvitation(userId: Long, familyId: Long): InvitationResponseDto =
        invitationService.sendQrInvitation(userId, familyId, CreateQrInvitationRequestDto())

    suspend fun getInvitations(userId: Long): List<InvitationResponseDto> =
        invitationService.getInvitations(userId)

    suspend fun getPendingInvitations(userId: Long): List<InvitationResponseDto> =
        invitationService.getPendingInvitations(userId)

    suspend fun acceptInvitation(userId: Long, invitationId: Long): InvitationResponseDto =
        invitationService.acceptInvitation(userId, invitationId)

    suspend fun rejectInvitation(userId: Long, invitationId: Long): InvitationResponseDto =
        invitationService.rejectInvitation(userId, invitationId)

    suspend fun getInvitationQr(familyId: Long): QrCodeResponseDto =
        invitationService.getInvitationQr(familyId)
}
