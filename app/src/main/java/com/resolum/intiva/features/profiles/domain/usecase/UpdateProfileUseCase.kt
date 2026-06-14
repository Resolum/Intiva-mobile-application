package com.resolum.intiva.features.profiles.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.domain.models.Profile
import com.resolum.intiva.features.profiles.domain.repositories.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: Long, name: String, bio: String, phoneNumber: String): NetworkResult<Profile> {
        return repository.updateProfile(userId, name, bio, phoneNumber)
    }
}
