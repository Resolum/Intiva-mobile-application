package com.resolum.intiva.features.iam.data.remote

import com.resolum.intiva.features.iam.data.remote.models.SignInRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.data.remote.services.AuthService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * Facade service for authentication-related operations.
 *
 * This service abstracts the underlying authentication mechanisms and provides a simple interface
 * for the rest of the application to interact with. It handles user registration and login by
 * delegating the requests to [AuthService].
 */
class AuthFacadeService @Inject constructor(
    private val authService: AuthService
) {

    /**
     * Signs up a new user with the provided [SignUpRequestDto].
     *
     * The backend expects the sign-up request as multipart/form-data because the avatar file
     * is optional. Since the current mobile screen does not select an avatar image yet,
     * avatarFile is sent as null.
     *
     * @param request The sign-up request containing email, password, name, age, phone number and bio.
     * @return A [SignUpResponseDto] with the created user's information.
     */
    suspend fun signUp(request: SignUpRequestDto): SignUpResponseDto {
        return authService.signUp(
            email = request.email.toPlainRequestBody(),
            password = request.password.toPlainRequestBody(),
            name = request.name.toPlainRequestBody(),
            age = request.age.toString().toPlainRequestBody(),
            phoneNumber = request.phoneNumber.toPlainRequestBody(),
            bio = request.bio.toPlainRequestBody(),
            avatarFile = null
        )
    }

    /**
     * Signs in an existing user with the provided [SignInRequestDto].
     *
     * Sign-in still uses a normal JSON body, unlike sign-up.
     *
     * @param request The sign-in request containing the user's email and password.
     * @return A [SignInResponseDto] with the result of the sign-in operation.
     */
    suspend fun signIn(request: SignInRequestDto): SignInResponseDto {
        return authService.signIn(request)
    }

    /**
     * Converts a String value into a plain text [RequestBody] for multipart requests.
     *
     * @return A [RequestBody] with text/plain media type.
     */
    private fun String.toPlainRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaType())
    }
}