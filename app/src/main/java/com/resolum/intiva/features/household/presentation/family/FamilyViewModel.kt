package com.resolum.intiva.features.household.presentation.family

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.usecase.AcceptInvitationByTokenUseCase
import com.resolum.intiva.features.household.domain.usecase.CreateFamilyUseCase
import com.resolum.intiva.features.household.domain.usecase.GetFamilyByIdUseCase
import com.resolum.intiva.features.household.domain.usecase.GetFamilyMembersUseCase
import com.resolum.intiva.features.household.domain.usecase.GetInvitationQrUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.savings.domain.repositories.SavingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val getFamilyByIdUseCase: GetFamilyByIdUseCase,
    private val getFamilyMembersUseCase: GetFamilyMembersUseCase,
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val getInvitationQrUseCase: GetInvitationQrUseCase,
    private val acceptInvitationByTokenUseCase: AcceptInvitationByTokenUseCase,
    private val sessionRepository: SessionRepository,
    private val savingGoalRepository: SavingGoalRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()

    fun loadFamilyQrCode() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) {
                _uiState.update { it.copy(qrCodeState = UiState.Error("No hay grupo familiar activo")) }
                return@safeLaunch
            }

            _uiState.update { it.copy(qrCodeState = UiState.Loading) }

            when (val result = getInvitationQrUseCase(groupId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(qrCodeState = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(qrCodeState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun acceptInvitationByToken(token: String) {
        safeLaunch {
            _uiState.update { it.copy(scanResultState = UiState.Loading) }

            when (val result = acceptInvitationByTokenUseCase(token)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(scanResultState = UiState.Success("Te uniste al grupo exitosamente")) }
                    loadFamily()
                    loadMembers()
                    loadBalance()
                }
                is NetworkResult.Error -> {
                    val message = when (result.code) {
                        409 -> "Ya eres miembro de este grupo"
                        410 -> "Este QR ha expirado"
                        else -> result.message
                    }
                    _uiState.update { it.copy(scanResultState = UiState.Error(message = message)) }
                }
            }
        }
    }

    fun resetScanResult() {
        _uiState.update { it.copy(scanResultState = UiState.Idle) }
    }

    fun loadFamily() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) {
                _uiState.update { it.copy(familyState = UiState.Idle) }
                return@safeLaunch
            }

            _uiState.update { it.copy(familyState = UiState.Loading) }

            when (val result = getFamilyByIdUseCase(groupId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(familyState = UiState.Success(result.data)) }
                    loadBalance()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(familyState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun loadMembers() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) {
                _uiState.update { it.copy(membersState = UiState.Idle) }
                return@safeLaunch
            }

            _uiState.update { it.copy(membersState = UiState.Loading) }

            when (val result = getFamilyMembersUseCase(groupId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(membersState = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(membersState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    private fun loadBalance() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId() ?: return@safeLaunch

            when (val result = savingGoalRepository.getGroupSavingGoals(groupId.toString())) {
                is NetworkResult.Success -> {
                    val total = result.data.sumOf { it.currentAmount.toDouble() }
                    val formatted = "S/ ${"%.2f".format(total)}"
                    _uiState.update { it.copy(totalBalance = formatted) }
                }
                is NetworkResult.Error -> {
                }
            }
        }
    }

    fun createFamily(name: String, description: String) {
        safeLaunch {
            _uiState.update { it.copy(familyState = UiState.Loading) }

            when (val result = createFamilyUseCase(CreateFamilyRequest(name, description))) {
                is NetworkResult.Success -> {
                    sessionRepository.saveGroupId(result.data.id)
                    _uiState.update { it.copy(familyState = UiState.Success(result.data)) }
                    loadMembers()
                    loadBalance()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(familyState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                familyState = UiState.Error(message = throwable.message ?: "Unexpected error", throwable = throwable)
            )
        }
    }
}
