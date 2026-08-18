package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

// ── Destination summary (GET /destinations) ──
data class DestinationSummary(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("summary") val summary: String?,
    @SerializedName("heroImageUrl") val heroImageUrl: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("avgCostMinMillion") val avgCostMinMillion: Int?,
    @SerializedName("avgCostMaxMillion") val avgCostMaxMillion: Int?,
    @SerializedName("avgTemperatureC") val avgTemperatureC: Int?,
    @SerializedName("idealDaysMin") val idealDaysMin: Int?,
    @SerializedName("idealDaysMax") val idealDaysMax: Int?,
    @SerializedName("bestTimeLabel") val bestTimeLabel: String?,
    @SerializedName("locationLabel") val locationLabel: String?,
    @SerializedName("featured") val featured: Boolean?,
    @SerializedName("published") val published: Boolean?,
    @SerializedName("types") val types: List<String>?,
    @SerializedName("highlightSpots") val highlightSpots: List<String>?
)

data class ApiResponseListDestination(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<DestinationSummary>?
)

// ── Destination detail (GET /destinations/{slug}) ──
data class DestinationDetail(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("summary") val summary: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("heroImageUrl") val heroImageUrl: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("avgCostMinMillion") val avgCostMinMillion: Int?,
    @SerializedName("avgCostMaxMillion") val avgCostMaxMillion: Int?,
    @SerializedName("avgTemperatureC") val avgTemperatureC: Int?,
    @SerializedName("idealDaysMin") val idealDaysMin: Int?,
    @SerializedName("idealDaysMax") val idealDaysMax: Int?,
    @SerializedName("bestTimeLabel") val bestTimeLabel: String?,
    @SerializedName("locationLabel") val locationLabel: String?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("weatherNow") val weatherNow: String?,
    @SerializedName("weatherForecast") val weatherForecast: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("types") val types: List<String>?,
    @SerializedName("highlightSpots") val highlightSpots: List<String>?,
    @SerializedName("attractions") val attractions: List<DestinationAttraction>?,
    @SerializedName("costItems") val costItems: List<DestinationCostItem>?,
    @SerializedName("mapPois") val mapPois: List<DestinationMapPoi>?,
    @SerializedName("reviews") val reviews: List<DestinationReview>?,
    @SerializedName("suggestedTours") val suggestedTours: List<TourSuggestion>?,
    @SerializedName("floraSuggestion") val floraSuggestion: FloraMatch?
)

data class DestinationAttraction(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("ticketPriceLabel") val ticketPriceLabel: String?,
    @SerializedName("openHours") val openHours: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)

data class DestinationCostItem(
    @SerializedName("category") val category: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("costMinMillion") val costMinMillion: Int?,
    @SerializedName("costMaxMillion") val costMaxMillion: Int?
)

data class DestinationMapPoi(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String?,
    @SerializedName("tier") val tier: String?,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("priceLabel") val priceLabel: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)

data class DestinationReview(
    @SerializedName("id") val id: String,
    @SerializedName("authorName") val authorName: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("comment") val comment: String?
)

data class TourSuggestion(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("durationLabel") val durationLabel: String?,
    @SerializedName("basePrice") val basePrice: Double?
)

data class FloraMatch(
    @SerializedName("destinationSlug") val destinationSlug: String?,
    @SerializedName("destinationName") val destinationName: String?,
    @SerializedName("matchPercent") val matchPercent: Int,
    @SerializedName("matchedPreferences") val matchedPreferences: List<String>?,
    @SerializedName("message") val message: String?
)

data class ApiResponseDestinationDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: DestinationDetail?
)

// ── Festivals (GET /destinations/festivals) ──
data class ThaiFestival(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("monthLabel") val monthLabel: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("imageUrl") val imageUrl: String?
)

data class ApiResponseListFestival(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<ThaiFestival>?
)

data class ThaiFestivalDetail(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("monthLabel") val monthLabel: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("longDescription") val longDescription: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("relatedDestinationSlug") val relatedDestinationSlug: String?,
    @SerializedName("relatedDestinationName") val relatedDestinationName: String?,
    @SerializedName("tips") val tips: List<String>?
)

data class ApiResponseFestivalDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ThaiFestivalDetail?
)
