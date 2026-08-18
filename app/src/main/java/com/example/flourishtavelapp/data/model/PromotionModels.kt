package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

// GET /promotions/active
data class Promotion(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String?,
    @SerializedName("discountType") val discountType: String?,
    @SerializedName("discountValue") val discountValue: Double?,
    @SerializedName("minOrderAmount") val minOrderAmount: Double?,
    @SerializedName("maxDiscountAmount") val maxDiscountAmount: Double?,
    @SerializedName("validFrom") val validFrom: String?,
    @SerializedName("validTo") val validTo: String?,
    @SerializedName("usageLimit") val usageLimit: Int?,
    @SerializedName("usedCount") val usedCount: Int?,
    @SerializedName("isActive") val isActive: Boolean?
)

data class ApiResponseListPromotion(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Promotion>?
)
