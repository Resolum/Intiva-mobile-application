package com.resolum.intiva.features.household.presentation.invitation

import com.resolum.intiva.features.household.domain.models.InvitationDetail

sealed class InvitationDetailUiState {
    data object Loading : InvitationDetailUiState()
    data class Success(
        val invitation: InvitationDetail,
        val isUserLoggedIn: Boolean
    ) : InvitationDetailUiState()
    data class Expired(val message: String) : InvitationDetailUiState()
    data class Error(val message: String) : InvitationDetailUiState()
}
