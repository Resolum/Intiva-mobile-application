package com.resolum.intiva.features.finances.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the design attributes of a category, such as color and icon.
 *
 * @property categoryColor The color associated with the category, typically in hex format (e.g., "#FF5733").
 * @property categoryIcon The name or identifier of the icon associated with the category.
 */
data class CategoryDesignResponseDto(
    @SerializedName("categoryColor")
    val categoryColor: String,
    @SerializedName("categoryIcon")
    val categoryIcon: String
)