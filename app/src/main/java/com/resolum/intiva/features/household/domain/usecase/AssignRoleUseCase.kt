package com.resolum.intiva.features.household.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.FamilyMember
import com.resolum.intiva.features.household.domain.models.FamilyRole
import com.resolum.intiva.features.household.domain.repositories.FamilyMemberRepository
import jakarta.inject.Inject

class AssignRoleUseCase @Inject constructor(
    private val familyMemberRepository: FamilyMemberRepository
) {
    suspend operator fun invoke(familyId: Long, memberId: Long, role: String): NetworkResult<FamilyMember> {
        if (familyId <= 0L) {
            return NetworkResult.Error("Family ID must be greater than zero")
        }
        if (memberId <= 0L) {
            return NetworkResult.Error("Member ID must be greater than zero")
        }
        if (FamilyRole.entries.none { it.name == role }) {
            return NetworkResult.Error("Invalid role: $role")
        }
        return familyMemberRepository.assignRole(familyId, memberId, role)
    }
}
