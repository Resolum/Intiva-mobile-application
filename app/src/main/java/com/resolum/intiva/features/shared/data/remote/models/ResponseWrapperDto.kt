package com.resolum.intiva.features.shared.data.remote.models

import com.google.gson.annotations.SerializedName

data class ResponseWrapperDto<T>(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T
)
