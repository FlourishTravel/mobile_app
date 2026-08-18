package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class FloraJourneyResponse(
    val success: Boolean,
    val data: FloraJourneyDto?
)

data class FloraJourneyDto(
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("sessionStartDate") val sessionStartDate: String?,
    @SerializedName("sessionEndDate") val sessionEndDate: String?,
    @SerializedName("bookingStatus") val bookingStatus: String? = null,
    @SerializedName("journeyStatus") val journeyStatus: String?,
    @SerializedName("currentActivity") val currentActivity: FloraActivityDto?,
    @SerializedName("nextActivity") val nextActivity: FloraActivityDto?,
    @SerializedName("nextMeeting") val nextMeeting: FloraNextMeetingDto?,
    @SerializedName("minutesUntilGathering") val minutesUntilGathering: Long?,
    @SerializedName("meetingPoint") val meetingPoint: String?,
    @SerializedName("warnings") val warnings: List<String>?,
    @SerializedName("currentScheduleItem") val currentScheduleItem: FloraScheduleItemDto?,
    @SerializedName("nextScheduleItem") val nextScheduleItem: FloraScheduleItemDto?
)

data class FloraActivityDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("startAt") val startAt: String?,
    @SerializedName("endAt") val endAt: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?,
    @SerializedName("scheduleSource") val scheduleSource: String?,
    @SerializedName("scheduleVersion") val scheduleVersion: Int?,
    @SerializedName("lastUpdatedAt") val lastUpdatedAt: String?,
    @SerializedName("lastUpdatedReason") val lastUpdatedReason: String?
)

data class FloraNextMeetingDto(
    @SerializedName("time") val time: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("minutesUntil") val minutesUntil: Long?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?,
    @SerializedName("reminderEligible") val reminderEligible: Boolean?,
    @SerializedName("scheduleSource") val scheduleSource: String?,
    @SerializedName("scheduleVersion") val scheduleVersion: Int?,
    @SerializedName("lastUpdatedAt") val lastUpdatedAt: String?,
    @SerializedName("lastUpdatedReason") val lastUpdatedReason: String?
)

data class FloraScheduleItemDto(
    @SerializedName("dayNumber") val dayNumber: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("summary") val summary: String?
)

data class FloraLocationRequest(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("accuracyMeters") val accuracyMeters: Double? = null
)

data class FloraLocationResponse(
    @SerializedName("accepted") val accepted: Boolean,
    @SerializedName("message") val message: String?
)

data class FloraLocationApiResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: FloraLocationResponse?
)
