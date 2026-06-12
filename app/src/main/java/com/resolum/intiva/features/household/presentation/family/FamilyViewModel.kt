package com.resolum.intiva.features.household.presentation.family

import androidx.lifecycle.viewModelScope
import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.usecase.CreateFamilyUseCase
import com.resolum.intiva.features.household.domain.usecase.GetFamilyByIdUseCase
import com.resolum.intiva.features.household.domain.usecase.GetFamilyMembersUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
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
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()

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

    fun createFamily(name: String, description: String) {
        safeLaunch {
            _uiState.update { it.copy(familyState = UiState.Loading) }

            when (val result = createFamilyUseCase(CreateFamilyRequest(name, description))) {
                is NetworkResult.Success -> {
                    sessionRepository.saveGroupId(result.data.id)
                    _uiState.update { it.copy(familyState = UiState.Success(result.data)) }
                    loadMembers()
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
