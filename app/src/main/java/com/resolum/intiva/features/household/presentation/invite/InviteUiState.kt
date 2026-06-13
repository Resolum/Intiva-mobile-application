package com.resolum.intiva.features.household.presentation.invite

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.deeplink.DeepLinkData
import com.resolum.intiva.features.household.domain.models.InvitationDetail
import com.resolum.intiva.features.household.domain.models.QrCodeResult

data class InviteUiState(
    val qrState: UiState<QrCodeResult> = UiState.Idle,
    val noActiveInvitation: Boolean = false,
    val isCreatingInvitation: Boolean = false,
    val createError: String? = null,
    val sendInvitationState: UiState<Unit> = UiState.Idle,
    val error: String? = null,
    val invitationDetail: UiState<InvitationDetail> = UiState.Idle,
    val actionState: UiState<String> = UiState.Idle,
    val deferredState: UiState<DeepLinkData> = UiState.Idle
)

