package com.resolum.intiva.features.household.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.FamilyMember

interface FamilyMemberRepository {

    suspend fun getFamilyMembers(familyId: Long): NetworkResult<List<FamilyMember>>

    suspend fun getFamilyMemberById(familyId: Long, memberId: Long): NetworkResult<FamilyMember>

    suspend fun assignRole(familyId: Long, memberId: Long, role: String): NetworkResult<FamilyMember>
}
