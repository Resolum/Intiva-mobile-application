package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Wrapper DTO for the GET members endpoint.
 * API returns: {"familyId": 2, "members": [...], "totalMembers": 1, "isEmpty": false}
 */
data class FamilyMembersResponseDto(
    @SerializedName("familyId") val familyId: Long,
    @SerializedName("members") val members: List<FamilyMemberResponseDto>,
    @SerializedName("totalMembers") val totalMembers: Int,
    @SerializedName("isEmpty") val isEmpty: Boolean
)
