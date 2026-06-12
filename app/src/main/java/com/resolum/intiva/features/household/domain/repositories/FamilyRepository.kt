package com.resolum.intiva.features.household.domain.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.models.Family

interface FamilyRepository {

    suspend fun createFamily(request: CreateFamilyRequest): NetworkResult<Family>

    suspend fun getFamilyById(id: Long): NetworkResult<Family>
}
