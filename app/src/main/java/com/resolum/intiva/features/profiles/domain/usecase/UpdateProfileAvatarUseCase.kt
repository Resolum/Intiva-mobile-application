package com.resolum.intiva.features.profiles.domain.usecase

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.profiles.domain.models.Profile
import com.resolum.intiva.features.profiles.domain.repositories.ProfileRepository
import java.io.File
import javax.inject.Inject

class UpdateProfileAvatarUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: Long, imageFile: File): NetworkResult<Profile> {
        return repository.updateAvatar(userId, imageFile)
    }
}
