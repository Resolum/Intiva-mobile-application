package com.resolum.intiva.features.household.presentation.invitation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.usecase.AcceptInvitationUseCase
import com.resolum.intiva.features.household.domain.usecase.GetPendingInvitationsUseCase
import com.resolum.intiva.features.household.domain.usecase.RejectInvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val getPendingInvitationsUseCase: GetPendingInvitationsUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(InvitationUiState())
    val uiState: StateFlow<InvitationUiState> = _uiState.asStateFlow()

    fun loadPendingInvitations() {
        safeLaunch {
            _uiState.update { it.copy(pendingInvitationsState = UiState.Loading) }

            when (val result = getPendingInvitationsUseCase()) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(pendingInvitationsState = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(pendingInvitationsState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun acceptInvitation(invitationId: Long) {
        safeLaunch {
            _uiState.update { it.copy(acceptState = UiState.Loading) }

            when (val result = acceptInvitationUseCase(invitationId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(acceptState = UiState.Success(Unit)) }
                    loadPendingInvitations()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(acceptState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun rejectInvitation(invitationId: Long) {
        safeLaunch {
            _uiState.update { it.copy(rejectState = UiState.Loading) }

            when (val result = rejectInvitationUseCase(invitationId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(rejectState = UiState.Success(Unit)) }
                    loadPendingInvitations()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(rejectState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(error = throwable.message ?: "Unexpected error")
        }
    }
}
