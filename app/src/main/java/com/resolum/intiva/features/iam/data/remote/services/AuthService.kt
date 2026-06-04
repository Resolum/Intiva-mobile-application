package com.resolum.intiva.features.iam.data.remote.services

import com.resolum.intiva.features.iam.data.remote.models.SignInRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignInResponseDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for authentication-related API calls.
 * This interface defines the endpoints for user authentication, such as signing up a new user.
 */
interface AuthService {

    /**
     * Makes a POST request to the "authentication/sign-up" endpoint to register a new user.
     *
     * @param body The request body containing the user's sign-up information.
     * @return A [Response] containing the [SignUpResponseDto] if successful, or an error response if not.
     */
    @POST("authentication/sign-up")
    suspend fun signUp(@Body body: SignUpRequestDto) : SignUpResponseDto

    /**
     * Makes a POST request to the "authentication/sign-in" endpoint to authenticate an existing user.
     *
     * @param body The request body containing the user's sign-in information.
     * @return A [Response] containing the [SignInResponseDto] if successful, or an error response if not.
     */
    @POST("authentication/sign-in")
    suspend fun signIn(@Body body: SignInRequestDto) : SignInResponseDto
}