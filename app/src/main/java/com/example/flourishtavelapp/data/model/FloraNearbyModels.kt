package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class FloraNearbyRecommendationResponse(
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("locationSource") val locationSource: String?,
    @SerializedName("locationLabel") val locationLabel: String?,
    @SerializedName("journeyContext") val journeyContext: FloraNearbyJourneyContext?,
    @SerializedName("recommendations") val recommendations: List<FloraNearbyItem>?,
    @SerializedName("warnings") val warnings: List<String>?
)

data class FloraNearbyJourneyContext(
    @SerializedName("currentActivityTitle") val currentActivityTitle: String?,
    @SerializedName("nextMeetingTime") val nextMeetingTime: String?,
    @SerializedName("nextMeetingLocation") val nextMeetingLocation: String?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?,
    @SerializedName("freeMinutesUntilMeeting") val freeMinutesUntilMeeting: Long?,
    @SerializedName("canValidateSchedule") val canValidateSchedule: Boolean?
)

data class FloraNearbyItem(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("dataSource") val dataSource: String?,
    @SerializedName("straightLineDistanceMeters") val straightLineDistanceMeters: Int?,
    @SerializedName("estimatedVisitMinutes") val estimatedVisitMinutes: Int?,
    @SerializedName("estimatedRoundTripMinutes") val estimatedRoundTripMinutes: Int?,
    @SerializedName("fitsSchedule") val fitsSchedule: Boolean?,
    @SerializedName("foodMatchStatus") val foodMatchStatus: String?,
    @SerializedName("budgetMatchStatus") val budgetMatchStatus: String?,
    @SerializedName("reason") val reason: String?,
    @SerializedName("warnings") val warnings: List<String>?,
    @SerializedName("mapAction") val mapAction: FloraMapAction?
)

data class FloraMapAction(
    @SerializedName("type") val type: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)

data class FloraNearbyRecommendationRequest(
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("radiusMeters") val radiusMeters: Int? = null,
    @SerializedName("limit") val limit: Int? = null,
    @SerializedName("categories") val categories: List<String>? = null
)

data class FloraNearbyApiResponse(
    val success: Boolean,
    val message: String?,
    val data: FloraNearbyRecommendationResponse?
)
