package com.resolum.intiva.features.household.presentation.roles

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.household.domain.models.FamilyMember

data class RolesUiState(
    val membersState: UiState<List<FamilyMember>> = UiState.Idle,
    val assignRoleState: UiState<FamilyMember> = UiState.Idle,
    val selectedRoleDescription: String = "",
    val error: String? = null
)
