package com.resolum.intiva.features.iam.data.remote.mappers

import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.domain.models.SignInResult
import com.resolum.intiva.features.iam.domain.models.SignUpResult

/**
 * Mapper to convert SignUpResponseDto to SignUpResult domain model.
 */
fun SignUpResponseDto.toDomain(): SignUpResult {
    return SignUpResult(
        id = id,
        email = email
    )
}

/**
 * Mapper to convert SignInResponseDto to SignInResult domain model.
 */
fun SignInResponseDto.toDomain(): SignInResult {
    return SignInResult(
        userId = userId,
        email = email,
        accessToken = token
    )
}
