package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class SiteContentDto(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("body") val body: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("category") val category: String?
)

data class ApiResponseListSiteContent(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<SiteContentDto>?
)

data class ContactRequestCreate(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("message") val message: String,
    @SerializedName("tourId") val tourId: String? = null
)

data class RequestRefundBody(
    @SerializedName("reason") val reason: String?
)

data class ApiResponseListTourSummaryDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<TourSummaryDto>?
)
