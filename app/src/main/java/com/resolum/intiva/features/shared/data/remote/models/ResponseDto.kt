package com.resolum.intiva.features.shared.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * A simple DTO to represent a response from the server that contains a message.
 * This can be used for various endpoints that return a message, such as success or error messages.
 * @param message The message returned from the server.
 */
data class ResponseDto(
    @SerializedName("message")
    val message: String
)
