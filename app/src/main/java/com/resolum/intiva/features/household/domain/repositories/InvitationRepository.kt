package com.resolum.intiva.features.household.domain.repositories

import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.Invitation
import com.resolum.intiva.features.household.domain.models.QrCodeResult

interface InvitationRepository {

    suspend fun sendInvitation(familyId: Long, userInvitedId: Long?): NetworkResult<Invitation>

    suspend fun getInvitations(): NetworkResult<List<Invitation>>

    suspend fun getPendingInvitations(): NetworkResult<List<Invitation>>

    suspend fun acceptInvitation(invitationId: Long): NetworkResult<Invitation>

    suspend fun rejectInvitation(invitationId: Long): NetworkResult<Invitation>

    suspend fun acceptInvitationByToken(token: String): NetworkResult<Unit>

    suspend fun rejectInvitationByToken(token: String): NetworkResult<String>

    suspend fun getDeferredInvitation(installId: String): NetworkResult<DeepLinkData>

    suspend fun getInvitationQr(familyId: Long): NetworkResult<QrCodeResult>
}
