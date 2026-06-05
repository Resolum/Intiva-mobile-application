package com.resolum.intiva.features.paymentmethodsandcategories.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a category response from the API.
 *
 * This class is used to parse the JSON response from the API into a Kotlin object. It contains
 * all the necessary fields that are expected in the category response, such as id, name, ownerType,
 * userId, groupId, isActive, description, color, and icon.
 */
data class CategoryResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("ownerType") val ownerType: String,
    @SerializedName("ownerId") val ownerId: Long,
    @SerializedName("groupId") val groupId: Long?,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("description") val description: String,
    @SerializedName("color") val color: String,
    @SerializedName("icon") val icon: String
)
