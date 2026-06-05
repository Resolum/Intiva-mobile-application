package com.resolum.intiva.features.iam.data.remote.mappers

import com.resolum.intiva.features.iam.data.remote.models.RollbackOnboardingRequestDto
import com.resolum.intiva.features.iam.domain.models.RollbackOnboardingRequest

/**
 * Mapper function to convert a [RollbackOnboardingRequestDto] to a [RollbackOnboardingRequest] domain model.
 *
 * @receiver RollbackOnboardingRequestDto The data transfer object representing the rollback onboarding request.
 * @return RollbackOnboardingRequest The domain model representing the rollback onboarding request.
 */
fun RollbackOnboardingRequestDto.toDomain(): RollbackOnboardingRequest {
    return RollbackOnboardingRequest(
        userId = userId
    )
}