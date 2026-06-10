package com.resolum.intiva.features.profiles.presentation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.profiles.domain.usecase.GetProfileByUserIdUseCase
import com.resolum.intiva.features.profiles.domain.usecase.UpdateProfileAvatarUseCase
import com.resolum.intiva.features.profiles.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileByUserIdUseCase: GetProfileByUserIdUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateProfileAvatarUseCase: UpdateProfileAvatarUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        safeLaunch {
            _uiState.update { it.copy(profileState = UiState.Loading) }

            val userId = sessionRepository.getUserId()
            if (userId == null) {
                _uiState.update {
                    it.copy(profileState = UiState.Error(message = "No se pudo obtener el usuario de la sesión."))
                }
                return@safeLaunch
            }

            when (val result = getProfileByUserIdUseCase(userId)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(profileState = UiState.Success(result.data)) }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(profileState = UiState.Error(message = result.message, throwable = result.throwable))
                    }
                }
            }
        }
    }

    fun updateProfile(name: String, bio: String, phoneNumber: String) {
        safeLaunch {
            _uiState.update { it.copy(updateState = UiState.Loading) }

            val userId = sessionRepository.getUserId()
            if (userId == null) {
                _uiState.update {
                    it.copy(updateState = UiState.Error(message = "No se pudo obtener el usuario de la sesión."))
                }
                return@safeLaunch
            }

            when (val result = updateProfileUseCase(userId, name, bio, phoneNumber)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(updateState = UiState.Success(result.data), profileState = UiState.Success(result.data))
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(updateState = UiState.Error(message = result.message, throwable = result.throwable))
                    }
                }
            }
        }
    }

    fun updateAvatar(imageFile: File) {
        safeLaunch {
            _uiState.update { it.copy(avatarUpdateState = UiState.Loading) }

            val userId = sessionRepository.getUserId()
            if (userId == null) {
                _uiState.update {
                    it.copy(avatarUpdateState = UiState.Error(message = "No se pudo obtener el usuario de la sesión."))
                }
                return@safeLaunch
            }

            when (val result = updateProfileAvatarUseCase(userId, imageFile)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(avatarUpdateState = UiState.Success(result.data), profileState = UiState.Success(result.data))
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(avatarUpdateState = UiState.Error(message = result.message, throwable = result.throwable))
                    }
                }
            }
        }
    }

    fun clearUpdateState() {
        _uiState.update { it.copy(updateState = UiState.Idle) }
    }

    fun clearAvatarUpdateState() {
        _uiState.update { it.copy(avatarUpdateState = UiState.Idle) }
    }

    fun logout(onSuccess: () -> Unit) {
        safeLaunch {
            sessionRepository.clearToken()
            onSuccess()
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(profileState = UiState.Error(message = throwable.message ?: "Error desconocido", throwable = throwable))
        }
    }
}
