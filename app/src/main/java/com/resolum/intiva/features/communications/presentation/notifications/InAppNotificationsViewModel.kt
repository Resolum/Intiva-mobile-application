package com.resolum.intiva.features.communications.presentation.notifications

import com.resolum.intiva.core.common.viewmodel.BaseViewModel
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.domain.models.InAppNotification
import com.resolum.intiva.features.communications.domain.usecase.GetNotificationsUseCase
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InAppNotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<InAppNotificationsScreenState>(InAppNotificationsScreenState.Loading)
    val uiState: StateFlow<InAppNotificationsScreenState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun refresh() {
        loadNotifications()
    }

    private fun loadNotifications() {
        safeLaunch {
            _uiState.value = InAppNotificationsScreenState.Loading

            val userId = sessionRepository.getUserId()
            if (userId == null) {
                _uiState.value = InAppNotificationsScreenState.Error(
                    message = "No se pudo obtener el usuario de la sesión."
                )
                return@safeLaunch
            }

            when (val result = getNotificationsUseCase(userId)) {
                is NetworkResult.Success -> {
                    val grouped = groupByDate(result.data)
                    _uiState.value = InAppNotificationsScreenState.Success(
                        groupedNotifications = grouped
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = InAppNotificationsScreenState.Error(
                        message = result.message
                    )
                }
            }
        }
    }

    private fun groupByDate(notifications: List<InAppNotification>): Map<String, List<InAppNotificationUiState>> {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale("es"))

        val sorted = notifications.sortedByDescending { it.createdAt }

        val groupedDomain = sorted.groupBy { notification ->
            val date = notification.createdAt.toLocalDate()
            when {
                date == today -> "Hoy"
                date == yesterday -> "Ayer"
                else -> date.format(dateFormatter)
            }
        }

        return groupedDomain.mapValues { (_, notifications) ->
            notifications.map { it.toUiState() }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.value = InAppNotificationsScreenState.Error(
            message = throwable.message ?: "Error inesperado"
        )
    }
}
