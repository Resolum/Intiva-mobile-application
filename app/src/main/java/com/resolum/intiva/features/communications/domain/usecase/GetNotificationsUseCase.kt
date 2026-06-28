package com.resolum.intiva.features.communications.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.communications.domain.models.InAppNotification
import com.resolum.intiva.features.communications.domain.repositories.InAppNotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: InAppNotificationRepository
) {
    suspend operator fun invoke(userId: Long): NetworkResult<List<InAppNotification>> {
        return repository.getNotifications(userId)
    }
}
