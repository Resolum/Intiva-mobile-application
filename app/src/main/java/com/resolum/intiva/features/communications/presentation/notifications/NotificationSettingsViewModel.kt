package com.resolum.intiva.features.communications.presentation.notifications

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationSettingsViewModel : ViewModel() {

    private val _sections = MutableStateFlow(buildDefaultSections())
    val sections: StateFlow<List<NotificationSection>> = _sections.asStateFlow()

    fun onToggle(itemId: String, enabled: Boolean) {
        _sections.value = _sections.value.map { section ->
            section.copy(items = section.items.map { item ->
                if (item.id == itemId) item.copy(enabled = enabled) else item
            })
        }
    }
}
