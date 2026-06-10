package com.resolum.intiva.features.profiles.data.remote.mappers

import com.resolum.intiva.features.profiles.data.remote.models.ProfileResponseDto
import com.resolum.intiva.features.profiles.domain.models.Profile

fun ProfileResponseDto.toDomain(): Profile {
    return Profile(
        id = this.id ?: 0L,
        name = this.name.orEmpty(),
        avatarUrl = this.avatarUrl.orEmpty(),
        email = this.email.orEmpty(),
        userId = this.userId ?: 0L,
        phoneNumber = this.phoneNumber.orEmpty(),
        bio = this.bio.orEmpty(),
        age = this.age ?: 0
    )
}
