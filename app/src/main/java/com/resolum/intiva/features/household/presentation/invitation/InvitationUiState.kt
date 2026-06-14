package com.resolum.intiva.features.household.presentation.invitation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.household.domain.models.Invitation

data class InvitationUiState(
    val pendingInvitationsState: UiState<List<Invitation>> = UiState.Idle,
    val acceptState: UiState<Unit> = UiState.Idle,
    val rejectState: UiState<Unit> = UiState.Idle,
    val error: String? = null
)
