package com.resolum.intiva.features.household.presentation.invitation

import androidx.lifecycle.SavedStateHandle
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.usecase.AcceptInvitationUseCase
import com.resolum.intiva.features.household.domain.usecase.GetInvitationByTokenUseCase
import com.resolum.intiva.features.household.domain.usecase.RejectInvitationUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InvitationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvitationByTokenUseCase: GetInvitationByTokenUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val token: String = savedStateHandle.get<String>("token") ?: ""

    private val _uiState = MutableStateFlow<InvitationDetailUiState>(InvitationDetailUiState.Loading)
    val uiState: StateFlow<InvitationDetailUiState> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<InvitationActionState>(InvitationActionState.Idle)
    val actionState: StateFlow<InvitationActionState> = _actionState.asStateFlow()

    init {
        loadInvitation()
    }

    private fun loadInvitation() {
        safeLaunch {
            _uiState.value = InvitationDetailUiState.Loading

            if (token.isBlank()) {
                _uiState.value = InvitationDetailUiState.Error("Token de invitación inválido")
                return@safeLaunch
            }

            val isLoggedIn = sessionRepository.getUserId() != null

            if (!isLoggedIn) {
                _uiState.value = InvitationDetailUiState.Error("Debes iniciar sesión para ver la invitación")
                return@safeLaunch
            }

            when (val result = getInvitationByTokenUseCase(token)) {
                is NetworkResult.Success -> {
                    _uiState.value = InvitationDetailUiState.Success(
                        invitation = result.data,
                        isUserLoggedIn = true
                    )
                }
                is NetworkResult.Error -> {
                    if (result.message.contains("expirado", ignoreCase = true) ||
                        result.message.contains("expiró", ignoreCase = true)
                    ) {
                        _uiState.value = InvitationDetailUiState.Expired(result.message)
                    } else {
                        _uiState.value = InvitationDetailUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun acceptInvitation(invitationId: Long) {
        safeLaunch {
            _actionState.value = InvitationActionState.Loading
            when (val result = acceptInvitationUseCase(invitationId)) {
                is NetworkResult.Success -> {
                    _actionState.value = InvitationActionState.Success("Invitación aceptada con éxito")
                }
                is NetworkResult.Error -> {
                    _actionState.value = InvitationActionState.Error(result.message)
                }
            }
        }
    }

    fun rejectInvitation(invitationId: Long) {
        safeLaunch {
            _actionState.value = InvitationActionState.Loading
            when (val result = rejectInvitationUseCase(invitationId)) {
                is NetworkResult.Success -> {
                    _actionState.value = InvitationActionState.Success("Invitación rechazada")
                }
                is NetworkResult.Error -> {
                    _actionState.value = InvitationActionState.Error(result.message)
                }
            }
        }
    }

    fun retry() {
        loadInvitation()
    }

    sealed class InvitationActionState {
        data object Idle : InvitationActionState()
        data object Loading : InvitationActionState()
        data class Success(val message: String) : InvitationActionState()
        data class Error(val message: String) : InvitationActionState()
    }
}
