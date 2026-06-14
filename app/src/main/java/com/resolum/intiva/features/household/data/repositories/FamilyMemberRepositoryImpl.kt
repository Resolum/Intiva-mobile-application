package com.resolum.intiva.features.household.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.data.remote.FamilyFacadeService
import com.resolum.intiva.features.household.data.remote.mappers.toDomain
import com.resolum.intiva.features.household.domain.models.FamilyMember
import com.resolum.intiva.features.household.domain.repositories.FamilyMemberRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

class FamilyMemberRepositoryImpl @Inject constructor(
    private val familyFacadeService: FamilyFacadeService,
    private val sessionRepository: SessionRepository
) : BaseRepository(), FamilyMemberRepository {

    override suspend fun getFamilyMembers(familyId: Long): NetworkResult<List<FamilyMember>> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.getFamilyMembers(userId, familyId).map { it.toDomain() }
    }

    override suspend fun getFamilyMemberById(familyId: Long, memberId: Long): NetworkResult<FamilyMember> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.getFamilyMemberById(userId, familyId, memberId).toDomain()
    }

    override suspend fun assignRole(familyId: Long, memberId: Long, role: String): NetworkResult<FamilyMember> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        val request = com.resolum.intiva.features.household.data.remote.models.AssignRoleRequestDto(role = role)
        familyFacadeService.assignRole(userId, familyId, memberId, request).toDomain()
    }
}
