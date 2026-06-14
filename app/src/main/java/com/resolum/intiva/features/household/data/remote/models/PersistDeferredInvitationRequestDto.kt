package com.resolum.intiva.features.household.data.remote.models

import com.google.gson.annotations.SerializedName

data class PersistDeferredInvitationRequestDto(
    @SerializedName("installId") val installId: String,
    @SerializedName("token") val token: String
)
