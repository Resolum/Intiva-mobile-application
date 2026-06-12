package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.FamilyMember
import com.resolum.intiva.features.household.domain.repositories.FamilyMemberRepository
import jakarta.inject.Inject

class GetFamilyMembersUseCase @Inject constructor(
    private val familyMemberRepository: FamilyMemberRepository
) {
    suspend operator fun invoke(familyId: Long): NetworkResult<List<FamilyMember>> {
        if (familyId <= 0L) {
            return NetworkResult.Error("Family ID must be greater than zero")
        }
        return familyMemberRepository.getFamilyMembers(familyId)
    }
}
