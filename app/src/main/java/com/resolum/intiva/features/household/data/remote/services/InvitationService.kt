package com.resolum.intiva.features.household.data.remote.services

import com.resolum.intiva.features.household.data.remote.models.CreateQrInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.DeferredInvitationResponseDto
import com.resolum.intiva.features.household.data.remote.models.InvitationRejectResponseDto
import com.resolum.intiva.features.household.data.remote.models.InvitationResponseDto
import com.resolum.intiva.features.household.data.remote.models.LinkInvitationResponseDto
import com.resolum.intiva.features.household.data.remote.models.PersistDeferredInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.PublicInvitationResponseDto
import com.resolum.intiva.features.household.data.remote.models.QrCodeResponseDto
import com.resolum.intiva.features.household.data.remote.models.SendInvitationRequestDto
import com.resolum.intiva.features.household.data.remote.models.SendLinkInvitationRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InvitationService {

    @POST("users/{userId}/families/{familyId}/invitations")
    suspend fun sendInvitation(
        @Path("userId") userId: Long,
        @Path("familyId") familyId: Long,
        @Body body: SendInvitationRequestDto
    ): InvitationResponseDto

    /**
     * Creates an open (QR-based) invitation with no target user.
     * Uses [CreateQrInvitationRequestDto] (empty body `{}`) to avoid sending
     * `userInvitedId: null`, which would violate the backend NOT NULL constraint.
     */
    @POST("users/{userId}/families/{familyId}/invitations")
    suspend fun sendQrInvitation(
        @Path("userId") userId: Long,
        @Path("familyId") familyId: Long,
        @Body body: CreateQrInvitationRequestDto
    ): InvitationResponseDto

    @GET("users/{userId}/invitations")
    suspend fun getInvitations(
        @Path("userId") userId: Long
    ): List<InvitationResponseDto>

    @GET("users/{userId}/invitations/pending")
    suspend fun getPendingInvitations(
        @Path("userId") userId: Long
    ): List<InvitationResponseDto>

    @PATCH("users/{userId}/invitations/{invitationId}/accept")
    suspend fun acceptInvitation(
        @Path("userId") userId: Long,
        @Path("invitationId") invitationId: Long
    ): InvitationResponseDto

    @PATCH("users/{userId}/invitations/{invitationId}/reject")
    suspend fun rejectInvitation(
        @Path("userId") userId: Long,
        @Path("invitationId") invitationId: Long
    ): InvitationResponseDto

    @PATCH("invitations/{token}/accept")
    suspend fun acceptInvitationByToken(
        @Path("token") token: String
    ): InvitationResponseDto

    @PATCH("invitations/{token}/reject")
    suspend fun rejectInvitationByToken(
        @Path("token") token: String
    ): InvitationRejectResponseDto

    @GET("invitations/deferred")
    suspend fun getDeferredInvitation(
        @Query("installId") installId: String
    ): DeferredInvitationResponseDto

    @GET("families/{familyId}/invitations/qr")
    suspend fun getInvitationQr(
        @Path("familyId") familyId: Long
    ): QrCodeResponseDto

    @POST("users/{userId}/invitations/link")
    suspend fun sendLinkInvitation(
        @Path("userId") userId: Long,
        @Body body: SendLinkInvitationRequestDto
    ): LinkInvitationResponseDto

    @POST("invitations/deferred")
    suspend fun persistDeferredInvitation(
        @Body body: PersistDeferredInvitationRequestDto
    )

    @GET("invitations/public/{token}")
    suspend fun getPublicInvitation(
        @Path("token") token: String
    ): PublicInvitationResponseDto
}
