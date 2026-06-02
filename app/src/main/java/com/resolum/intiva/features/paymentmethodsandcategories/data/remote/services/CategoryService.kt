package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CategoryResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateCategoryRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for fetching category data from the API.
 *
 * This interface defines the endpoints and HTTP methods for interacting with category-related
 * resources on the server. It includes a method to retrieve categories based on a user's ID.
 */
interface CategoryService {

    /**
     * Fetches a list of categories associated with a specific user ID.
     *
     * @param userId The ID of the user whose categories are to be retrieved.
     * @return A list of [CategoryResponseDto] objects representing the user's categories.
     */
    @GET("users/{userId}/categories")
    suspend fun getCategoriesByUserId(
        @Path("userId") userId: Long
    ) : List<CategoryResponseDto>

    @POST("users/{userId}/categories")
    suspend fun createCategory(
        @Path("userId") userId: Long,
        @Body request: CreateCategoryRequestDto
    ): CategoryResponseDto
}