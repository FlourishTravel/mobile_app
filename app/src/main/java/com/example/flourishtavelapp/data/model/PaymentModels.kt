package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class MomoPayUrlResponse(
    @SerializedName("paymentUrl") val paymentUrl: String
)

data class ApiResponseMomoPayUrlResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: MomoPayUrlResponse?
)
