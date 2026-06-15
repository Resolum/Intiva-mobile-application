package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.Family
import com.resolum.intiva.features.household.domain.repositories.FamilyRepository
import jakarta.inject.Inject

class GetFamilyByIdUseCase @Inject constructor(
    private val familyRepository: FamilyRepository
) {
    suspend operator fun invoke(id: Long): NetworkResult<Family> {
        if (id <= 0L) {
            return NetworkResult.Error("Family ID must be greater than zero")
        }
        return familyRepository.getFamilyById(id)
    }
}
