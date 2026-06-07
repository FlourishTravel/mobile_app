package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

data class Tour(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String?,
    @SerializedName("basePrice") val basePrice: Double,
    @SerializedName("durationDays") val durationDays: Int,
    @SerializedName("durationNights") val durationNights: Int,
    @SerializedName("destinationCity") val destinationCity: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("badge") val badge: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("featured") val featured: Boolean?,
    @SerializedName("highlightsText") val highlightsText: String?,
    @SerializedName("includesText") val includesText: String?,
    @SerializedName("excludesText") val excludesText: String?
)

data class ApiResponseListTour(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Tour>?
)

data class FavoriteRequest(
    @SerializedName("tourId") val tourId: String
)
