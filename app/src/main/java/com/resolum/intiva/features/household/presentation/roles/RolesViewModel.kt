package com.resolum.intiva.features.household.presentation.roles

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.FamilyRole
import com.resolum.intiva.features.household.domain.usecase.AssignRoleUseCase
import com.resolum.intiva.features.household.domain.usecase.GetFamilyMembersUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RolesViewModel @Inject constructor(
    private val getFamilyMembersUseCase: GetFamilyMembersUseCase,
    private val assignRoleUseCase: AssignRoleUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RolesUiState())
    val uiState: StateFlow<RolesUiState> = _uiState.asStateFlow()

    fun loadMembers() {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) {
                _uiState.update { it.copy(membersState = UiState.Error("No hay grupo familiar activo")) }
                return@safeLaunch
            }

            _uiState.update { it.copy(membersState = UiState.Loading) }

            when (val result = getFamilyMembersUseCase(groupId)) {
                is NetworkResult.Success -> {
                    val currentUserId = sessionRepository.getUserId()
                    val isAdmin = result.data.any { it.userId == currentUserId && it.role == FamilyRole.ADMIN.name }
                    _uiState.update { it.copy(membersState = UiState.Success(result.data), isCurrentUserAdmin = isAdmin) }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(membersState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun assignRole(memberId: Long, role: String) {
        safeLaunch {
            val groupId = sessionRepository.getGroupId()
            if (groupId == null) return@safeLaunch

            if (!_uiState.value.isCurrentUserAdmin) {
                _uiState.update { it.copy(assignRoleState = UiState.Error("Solo el administrador puede modificar roles")) }
                return@safeLaunch
            }

            _uiState.update { it.copy(assignRoleState = UiState.Loading) }

            when (val result = assignRoleUseCase(groupId, memberId, role)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(assignRoleState = UiState.Success(result.data)) }
                    loadMembers()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(assignRoleState = UiState.Error(message = result.message)) }
                }
            }
        }
    }

    fun getRoleDescription(role: String): String {
        return when (role) {
            FamilyRole.ADMIN.name -> "Puede gestionar miembros, asignar roles, crear metas y administrar la configuración del grupo."
            FamilyRole.MEMBER.name -> "Puede ver el progreso del grupo, registrar transacciones y participar en metas compartidas."
            else -> ""
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(error = throwable.message ?: "Unexpected error")
        }
    }
}
