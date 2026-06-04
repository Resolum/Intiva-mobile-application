package com.resolum.intiva.features.finances.domain.models

/**
 * Represents the design attributes of a financial category, including its color and icon.
 *
 * @property categoryColor The color associated with the category, typically represented as a hex string (e.g., "#FF5733").
 * @property categoryIcon The icon associated with the category, which could be a resource identifier or a URL to an image.
 */
data class CategoryDesign(
    val categoryColor: String,
    val categoryIcon: String
)
