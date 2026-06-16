package com.resolum.intiva.features.iam.data.repositories

import com.resolum.intiva.core.network.model.NetworkResult
import com.resolum.intiva.features.iam.data.remote.AuthFacadeService
import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import com.resolum.intiva.features.iam.data.remote.models.SignUpResponseDto
import com.resolum.intiva.features.iam.domain.models.SignUpRequest
import com.resolum.intiva.features.iam.domain.repositories.SessionRepository
import com.resolum.intiva.features.communications.domain.repositories.NotificationDeviceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryImplTest {

    private val authFacadeService = mockk<AuthFacadeService>()
    private val sessionRepository = mockk<SessionRepository>()
    private val notificationDeviceRepository = mockk<NotificationDeviceRepository>()

    private val authRepository = AuthRepositoryImpl(
        authFacadeService,
        sessionRepository,
        notificationDeviceRepository
    )

    @Test
    fun signUp(): Unit = runTest {

        val request = SignUpRequest("test@mail.com", "1234")

        coEvery {
            authFacadeService.signUp(any())
        } returns SignUpResponseDto(id = 1L, email = "test@mail.com")

        val result = authRepository.signUp(request)

        assertTrue(
            "The result should be a success: $result",
            result is NetworkResult.Success
        )
    }

}