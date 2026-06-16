package com.resolum.intiva.features.iam.data.remote.services

import com.resolum.intiva.features.iam.data.remote.models.SignUpRequestDto
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authService: AuthService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authService = retrofit.create(AuthService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `signUp should return success response`() = runTest {

        val mockResponse = """
        {
            "id": "1",
            "email": "test@mail.com"
        }
    """

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        val response = authService.signUp(
            SignUpRequestDto("test@mail.com", "1234")
        )

        val request = mockWebServer.takeRequest()

        assertEquals("/authentication/sign-up", request.path)
        assertEquals("POST", request.method)

        assertEquals("test@mail.com", response.email)
    }
}