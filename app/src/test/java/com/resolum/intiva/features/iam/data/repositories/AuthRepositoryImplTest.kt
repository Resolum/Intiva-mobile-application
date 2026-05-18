package com.resolum.intiva.features.iam.data.repositories

import androidx.camera.core.ImageProcessor
import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.data.remote.services.AuthService
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private val authService = mockk<AuthService>()

    private val authRepository = AuthRepositoryImpl(authService)

    @Test
    fun signUp(): Unit = runTest {

        val request = SignUpRequest("test@mail.com", "1234")

        coEvery {
            authService.signUp(any())
        } returns Response.success(
            SignUpResponseDto(id = "1", email = "test@mail.com")
        )

        val result = authRepository.signUp(request)

        assertTrue(
            "The result should be a success: $result",
            result is NetworkResult.Success
        )
    }

}