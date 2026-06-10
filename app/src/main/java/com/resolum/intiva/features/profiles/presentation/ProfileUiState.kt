package com.resolum.intiva.features.profiles.presentation

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.profiles.domain.models.Profile

data class ProfileUiState(
    val profileState: UiState<Profile> = UiState.Idle,
    val updateState: UiState<Profile> = UiState.Idle,
    val avatarUpdateState: UiState<Profile> = UiState.Idle
)
