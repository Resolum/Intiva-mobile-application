package com.resolum.intiva.features.household.data.remote.services

import com.resolum.intiva.features.household.data.remote.models.AssignRoleRequestDto
import com.resolum.intiva.features.household.data.remote.models.FamilyMemberResponseDto
import com.resolum.intiva.features.household.data.remote.models.FamilyMembersResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface FamilyMemberService {

    @GET("users/{userId}/families/{familyId}/members")
    suspend fun getFamilyMembers(
        @Path("userId") userId: Long,
        @Path("familyId") familyId: Long
    ): FamilyMembersResponseDto

    @GET("users/{userId}/families/{familyId}/members/{memberId}")
    suspend fun getFamilyMemberById(
        @Path("userId") userId: Long,
        @Path("familyId") familyId: Long,
        @Path("memberId") memberId: Long
    ): FamilyMemberResponseDto

    @PATCH("users/{userId}/families/{familyId}/members/{memberId}/role")
    suspend fun assignRole(
        @Path("userId") userId: Long,
        @Path("familyId") familyId: Long,
        @Path("memberId") memberId: Long,
        @Body body: AssignRoleRequestDto
    ): FamilyMemberResponseDto
}
