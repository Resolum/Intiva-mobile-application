package com.resolum.intiva.features.household.data.repositories

import com.resolum.intiva.core.data.repository.BaseRepository
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.household.data.remote.FamilyFacadeService
import com.resolum.intiva.features.household.data.remote.mappers.toDomain
import com.resolum.intiva.features.household.data.remote.mappers.toDto
import com.resolum.intiva.features.household.domain.models.CreateFamilyRequest
import com.resolum.intiva.features.household.domain.models.Family
import com.resolum.intiva.features.household.domain.repositories.FamilyRepository
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val familyFacadeService: FamilyFacadeService,
    private val sessionRepository: SessionRepository
) : BaseRepository(), FamilyRepository {

    override suspend fun createFamily(request: CreateFamilyRequest): NetworkResult<Family> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.createFamily(userId, request.toDto()).toDomain()
    }

    override suspend fun getFamilyById(id: Long): NetworkResult<Family> = safeCall {
        val userId = sessionRepository.getUserId() ?: throw IllegalStateException("User ID not found in session")
        familyFacadeService.getFamilyById(userId, id).toDomain()
    }
}
