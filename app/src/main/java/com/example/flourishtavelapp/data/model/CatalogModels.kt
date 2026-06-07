package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class ApiResponseListCategory(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Category>?
)

data class CategoryRef(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("archived") val archived: Boolean?
)

data class SessionRef(
    @SerializedName("id") val id: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("status") val status: String?,
    @SerializedName("maxParticipants") val maxParticipants: Int?,
    @SerializedName("currentParticipants") val currentParticipants: Int?
)

data class TourSummaryDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("basePrice") val basePrice: Double,
    @SerializedName("durationDays") val durationDays: Int?,
    @SerializedName("durationNights") val durationNights: Int?,
    @SerializedName("category") val category: CategoryRef?,
    @SerializedName("status") val status: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("earliestSession") val earliestSession: SessionRef?
)

data class PageTourSummaryDto(
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("content") val content: List<TourSummaryDto>,
    @SerializedName("number") val number: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("empty") val empty: Boolean
)

data class ApiResponsePageTourSummaryDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: PageTourSummaryDto?
)

data class ImageRef(
    @SerializedName("id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("caption") val caption: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)

data class VideoRef(
    @SerializedName("id") val id: String,
    @SerializedName("videoUrl") val videoUrl: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("durationSeconds") val durationSeconds: Int?,
    @SerializedName("sortOrder") val sortOrder: Int?
)

data class ActivityRef(
    @SerializedName("id") val id: String,
    @SerializedName("sortOrder") val sortOrder: Int,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("durationMinutes") val durationMinutes: Int?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("activityType") val activityType: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("costEstimate") val costEstimate: Double?,
    @SerializedName("costIncluded") val costIncluded: Boolean?,
    @SerializedName("tags") val tags: String?
)

data class ItineraryRef(
    @SerializedName("id") val id: String,
    @SerializedName("dayNumber") val dayNumber: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("coverImageUrl") val coverImageUrl: String?,
    @SerializedName("accommodation") val accommodation: String?,
    @SerializedName("transport") val transport: String?,
    @SerializedName("mealsIncluded") val mealsIncluded: String?,
    @SerializedName("highlights") val highlights: String?,
    @SerializedName("activities") val activities: List<ActivityRef>?
)

data class LocationRef(
    @SerializedName("id") val id: String,
    @SerializedName("locationName") val locationName: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("visitOrder") val visitOrder: Int,
    @SerializedName("dayNumber") val dayNumber: Int
)

data class GuideRef(
    @SerializedName("id") val id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("initials") val initials: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("active") val active: Boolean?
)

data class SessionDetail(
    @SerializedName("id") val id: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("maxParticipants") val maxParticipants: Int,
    @SerializedName("currentParticipants") val currentParticipants: Int,
    @SerializedName("status") val status: String,
    @SerializedName("tourGuide") val tourGuide: GuideRef?
)

data class ReviewRef(
    @SerializedName("authorName") val authorName: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("comment") val comment: String?
)

data class TourDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String?,
    @SerializedName("basePrice") val basePrice: Double,
    @SerializedName("durationDays") val durationDays: Int,
    @SerializedName("durationNights") val durationNights: Int,
    @SerializedName("category") val category: CategoryRef?,
    @SerializedName("status") val status: String?,
    @SerializedName("images") val images: List<ImageRef>?,
    @SerializedName("videos") val videos: List<VideoRef>?,
    @SerializedName("itineraries") val itineraries: List<ItineraryRef>?,
    @SerializedName("locations") val locations: List<LocationRef>?,
    @SerializedName("sessions") val sessions: List<SessionDetail>?,
    @SerializedName("destinationCity") val destinationCity: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("badge") val badge: String?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("highlights") val highlights: List<String>?,
    @SerializedName("includes") val includes: List<String>?,
    @SerializedName("excludes") val excludes: List<String>?,
    @SerializedName("reviews") val reviews: List<ReviewRef>?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class ApiResponseTourDetailDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: TourDetailDto?
)
