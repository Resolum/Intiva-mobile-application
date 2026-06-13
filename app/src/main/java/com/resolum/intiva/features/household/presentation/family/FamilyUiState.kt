package com.resolum.intiva.features.household.presentation.family

import com.resolum.intiva.core.common.state.UiState
import com.resolum.intiva.features.household.domain.models.Family
import com.resolum.intiva.features.household.domain.models.FamilyMember
import com.resolum.intiva.features.household.domain.models.QrCodeResult

data class FamilyUiState(
    val familyState: UiState<Family> = UiState.Idle,
    val membersState: UiState<List<FamilyMember>> = UiState.Idle,
    val qrCodeState: UiState<QrCodeResult> = UiState.Idle,
    val scanResultState: UiState<String> = UiState.Idle,
    val error: String? = null
)
