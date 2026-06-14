package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.services

import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CategoryResponseDto
import com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models.CreateCategoryRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for fetching category data from the API.
 *
 * This interface defines the endpoints and HTTP methods for interacting with category-related
 * resources on the server. It includes a method to retrieve categories based on a user's ID.
 */
interface CategoryService {

    @GET("categories")
    suspend fun getCategoriesByOwnerId(
        @Query("ownerType") ownerType: String,
        @Query("ownerId") ownerId: Long,
        @Query("type") type: String? = null
    ): List<CategoryResponseDto>

    @POST("categories")
    suspend fun createCategory(
        @Body request: CreateCategoryRequestDto
    ): CategoryResponseDto
}
