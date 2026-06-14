package com.resolum.intiva.features.household.presentation.invite

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.InvitationDetail
import com.resolum.intiva.features.household.domain.usecase.AcceptInvitationByTokenUseCase
import com.resolum.intiva.features.household.domain.usecase.GetDeferredInvitationUseCase
import com.resolum.intiva.features.household.domain.usecase.GetInvitationByTokenUseCase
import com.resolum.intiva.features.household.domain.usecase.GetInvitationQrUseCase
import com.resolum.intiva.features.household.domain.usecase.PersistDeferredInvitationUseCase
import com.resolum.intiva.features.household.domain.usecase.RejectInvitationByTokenUseCase
import com.resolum.intiva.features.household.domain.usecase.SendInvitationUseCase
import com.resolum.intiva.features.household.domain.usecase.SendLinkInvitationUseCase
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
    private val acceptInvitationByTokenUseCase: AcceptInvitationByTokenUseCase,
    private val getInvitationByTokenUseCase: GetInvitationByTokenUseCase,
    private val rejectInvitationByTokenUseCase: RejectInvitationByTokenUseCase,
    private val getDeferredInvitationUseCase: GetDeferredInvitationUseCase,
    private val sendLinkInvitationUseCase: SendLinkInvitationUseCase,
    private val persistDeferredInvitationUseCase: PersistDeferredInvitationUseCase,
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

    fun generateLinkInvitation() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId() ?: return@safeLaunch

            _uiState.update { it.copy(linkInviteState = UiState.Loading) }

            when (val result = sendLinkInvitationUseCase(groupId)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(linkInviteState = UiState.Success(result.data))
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(linkInviteState = UiState.Error(message = result.message))
                    }
                }
            }
        }
    }

    fun clearLinkInviteState() {
        _uiState.update { it.copy(linkInviteState = UiState.Idle) }
    }

    fun persistDeferredInvitation(installId: String, token: String) {
        safeLaunch {
            _uiState.update { it.copy(persistDeferredState = UiState.Loading) }

            when (val result = persistDeferredInvitationUseCase(installId, token)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(persistDeferredState = UiState.Success(Unit)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(persistDeferredState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun shareInvitation() {
        val link = getInvitationLink()

    }

    fun copyInvitationLink() {
        val link = getInvitationLink()

    }

    fun loadInvitationByToken(token: String) {
        safeLaunch {
            _uiState.update { it.copy(invitationDetail = UiState.Loading) }

            when (val result = getInvitationByTokenUseCase(token)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(invitationDetail = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(invitationDetail = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun rejectInvite(token: String) {
        safeLaunch {
            _uiState.update { it.copy(actionState = UiState.Loading) }

            when (val result = rejectInvitationByTokenUseCase(token)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(actionState = UiState.Success("Invitación rechazada")) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(actionState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun loadDeferredInvitation(installId: String) {
        safeLaunch {
            _uiState.update { it.copy(deferredState = UiState.Loading) }

            when (val result = getDeferredInvitationUseCase(installId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(deferredState = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(deferredState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun acceptInvite(token: String) {
        safeLaunch {
            _uiState.update { it.copy(actionState = UiState.Loading) }

            when (val result = acceptInvitationByTokenUseCase(token)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(actionState = UiState.Success("Invitación aceptada")) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(actionState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                qrState = UiState.Error(message = throwable.message ?: "Error", throwable = throwable),
                isCreatingInvitation = false,
                linkInviteState = UiState.Error(message = throwable.message ?: "Error", throwable = throwable)
            )
        }
    }
}

