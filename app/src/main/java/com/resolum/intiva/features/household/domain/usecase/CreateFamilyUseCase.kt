package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.models.Family
import com.resolum.intiva.features.household.domain.repositories.FamilyRepository
import jakarta.inject.Inject

class CreateFamilyUseCase @Inject constructor(
    private val familyRepository: FamilyRepository
) {
    suspend operator fun invoke(request: CreateFamilyRequest): NetworkResult<Family> {
        if (request.name.isBlank()) {
            return NetworkResult.Error("Family name is required")
        }
        return familyRepository.createFamily(request)
    }
}
