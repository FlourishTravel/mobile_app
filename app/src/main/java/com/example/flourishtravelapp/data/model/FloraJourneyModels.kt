package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class FloraJourneyResponse(
    val success: Boolean,
    val data: FloraJourneyDto?
)

data class FloraJourneyDto(
  @SerializedName("bookingId") val bookingId: String?,
  @SerializedName("tourTitle") val tourTitle: String?,
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
  @SerializedName("scheduleStatus") val scheduleStatus: String?
)

data class FloraNextMeetingDto(
  @SerializedName("time") val time: String?,
  @SerializedName("locationName") val locationName: String?,
  @SerializedName("locationAddress") val locationAddress: String?,
  @SerializedName("latitude") val latitude: Double?,
  @SerializedName("longitude") val longitude: Double?,
  @SerializedName("minutesUntil") val minutesUntil: Long?,
  @SerializedName("scheduleStatus") val scheduleStatus: String?,
  @SerializedName("reminderEligible") val reminderEligible: Boolean?
)

data class FloraScheduleItemDto(
  @SerializedName("dayNumber") val dayNumber: Int?,
  @SerializedName("title") val title: String?,
  @SerializedName("summary") val summary: String?
)
