package com.resolum.intiva.features.household.presentation.invite

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.usecase.GetInvitationQrUseCase
import com.resolum.intiva.features.household.domain.usecase.SendInvitationUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val getInvitationQrUseCase: GetInvitationQrUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(InviteUiState())
    val uiState: StateFlow<InviteUiState> = _uiState.asStateFlow()

    fun loadQrCode() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) {
                _uiState.update {
                    it.copy(
                        qrState = UiState.Error("No hay grupo familiar activo"),
                        noActiveInvitation = false
                    )
                }
                return@safeLaunch
            }

            _uiState.update { it.copy(qrState = UiState.Loading, noActiveInvitation = false) }

            when (val result = getInvitationQrUseCase(groupId)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            qrState = UiState.Success(result.data),
                            noActiveInvitation = false
                        )
                    }
                }
                is NetworkResult.Error -> {
                    if (result.code == 404) {
                        _uiState.update {
                            it.copy(
                                qrState = UiState.Idle,
                                noActiveInvitation = true,
                                createError = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                qrState = UiState.Error(message = result.message),
                                noActiveInvitation = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Asks the backend to create a new open QR invitation for the family and, on success,
     * reloads the QR code.
     *
     * This will work once the backend allows nullable [userInvitedId] (open invitations).
     */
    fun generateQrInvitation() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId() ?: return@safeLaunch

            _uiState.update {
                it.copy(isCreatingInvitation = true, createError = null)
            }

            when (val result = sendInvitationUseCase(groupId, userInvitedId = null)) {
                is NetworkResult.Success -> {
                    // Invitation created — now load the QR
                    _uiState.update { it.copy(isCreatingInvitation = false) }
                    loadQrCode()
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreatingInvitation = false,
                            createError = "No se pudo crear la invitación: ${result.message}"
                        )
                    }
                }
            }
        }
    }

    fun getInvitationLink(): String =
        (_uiState.value.qrState as? UiState.Success)?.data?.invitationLink ?: ""

    fun shareInvitation() {
        val link = getInvitationLink()
        // Handled by the UI layer (intent / share sheet)
    }

    fun copyInvitationLink() {
        val link = getInvitationLink()
        // Handled by the UI layer (clipboard manager)
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                qrState = UiState.Error(message = throwable.message ?: "Error", throwable = throwable),
                isCreatingInvitation = false
            )
        }
    }
}

