package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class GuideSessionSummaryDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("tourId") val tourId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("tourCode") val tourCode: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("currentParticipants") val currentParticipants: Int = 0,
    @SerializedName("maxParticipants") val maxParticipants: Int = 0,
    @SerializedName("checkedInParticipants") val checkedInParticipants: Int = 0
)

data class GuideItineraryActivityDto(
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("durationMinutes") val durationMinutes: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("activityType") val activityType: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("imageUrl") val imageUrl: String?
)

data class GuideItineraryDayDto(
    @SerializedName("dayNumber") val dayNumber: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("activities") val activities: List<GuideItineraryActivityDto>?
)

data class GuideSessionDetailDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("tourId") val tourId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("tourCode") val tourCode: String?,
    @SerializedName("tourDescription") val tourDescription: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("currentParticipants") val currentParticipants: Int = 0,
    @SerializedName("maxParticipants") val maxParticipants: Int = 0,
    @SerializedName("checkedInParticipants") val checkedInParticipants: Int = 0,
    @SerializedName("itineraryDays") val itineraryDays: List<GuideItineraryDayDto>?
)

data class GuideCompanionLineDto(
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("dateOfBirth") val dateOfBirth: String?,
    @SerializedName("maskedIdNumber") val maskedIdNumber: String?
)

data class GuideActivityAttendanceDto(
    @SerializedName("activityId") val activityId: String?,
    @SerializedName("checkInAt") val checkInAt: String?,
    @SerializedName("checkOutAt") val checkOutAt: String?
)

data class GuideParticipantAttendanceDto(
    @SerializedName("participantId") val participantId: String?,
    @SerializedName("rosterKey") val rosterKey: String?,
    @SerializedName("participantRole") val participantRole: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("phoneSnapshot") val phoneSnapshot: String?,
    @SerializedName("lineIndex") val lineIndex: Int = 0,
    @SerializedName("checkInAt") val checkInAt: String?,
    @SerializedName("checkOutAt") val checkOutAt: String?,
    @SerializedName("activityAttendance") val activityAttendance: List<GuideActivityAttendanceDto>?
)

data class GuideItineraryStopDto(
    @SerializedName("activityId") val activityId: String?,
    @SerializedName("dayNumber") val dayNumber: Int?,
    @SerializedName("dayTitle") val dayTitle: String?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("activityType") val activityType: String?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("checkedInAtStopCount") val checkedInAtStopCount: Int = 0
)

data class GuideGuestBookingRowDto(
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("travelerUserId") val travelerUserId: String?,
    @SerializedName("travelerName") val travelerName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("guestCount") val guestCount: Int = 1,
    @SerializedName("specialRequests") val specialRequests: String?,
    @SerializedName("effectiveContactPhone") val effectiveContactPhone: String?,
    @SerializedName("pickupAddress") val pickupAddress: String?,
    @SerializedName("emergencyContactName") val emergencyContactName: String?,
    @SerializedName("emergencyContactPhone") val emergencyContactPhone: String?,
    @SerializedName("checkedInGathering") val checkedInGathering: Boolean = false,
    @SerializedName("allParticipantsCheckedIn") val allParticipantsCheckedIn: Boolean = false,
    @SerializedName("companions") val companions: List<GuideCompanionLineDto>?,
    @SerializedName("participantAttendance") val participantAttendance: List<GuideParticipantAttendanceDto>?
)

data class GuideSessionGuestsDto(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("tourCode") val tourCode: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("totalGuestSlots") val totalGuestSlots: Int = 0,
    @SerializedName("checkedInGuestSlots") val checkedInGuestSlots: Int = 0,
    @SerializedName("checkedOutParticipants") val checkedOutParticipants: Int = 0,
    @SerializedName("paidBookingCount") val paidBookingCount: Int = 0,
    @SerializedName("bookingsWithSpecialRequests") val bookingsWithSpecialRequests: Int = 0,
    @SerializedName("itineraryStops") val itineraryStops: List<GuideItineraryStopDto>?,
    @SerializedName("bookings") val bookings: List<GuideGuestBookingRowDto>?
)

data class GuideCheckinRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("checkInType") val checkInType: String = "gathering"
)

data class SessionCheckinResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("checkInType") val checkInType: String?
)

data class SessionParticipantResultDto(
    @SerializedName("participantId") val participantId: String?,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("checkInAt") val checkInAt: String?,
    @SerializedName("checkOutAt") val checkOutAt: String?
)

data class ParticipantActivityAttendanceResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("sessionParticipantId") val sessionParticipantId: String?,
    @SerializedName("tourActivityId") val tourActivityId: String?,
    @SerializedName("checkInAt") val checkInAt: String?,
    @SerializedName("checkOutAt") val checkOutAt: String?
)

data class SessionScheduleTemplateDto(
    @SerializedName("title") val title: String?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("isGatheringEvent") val isGatheringEvent: Boolean?,
    @SerializedName("gatheringEventType") val gatheringEventType: String?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?
)

data class SessionScheduleOverrideDto(
    @SerializedName("publicationStatus") val publicationStatus: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("isGatheringEvent") val isGatheringEvent: Boolean?,
    @SerializedName("gatheringEventType") val gatheringEventType: String?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?,
    @SerializedName("operationalNote") val operationalNote: String?,
    @SerializedName("version") val version: Int?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("publishedByName") val publishedByName: String?
)

data class SessionScheduleEffectiveDto(
    @SerializedName("title") val title: String?,
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("endTime") val endTime: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("locationAddress") val locationAddress: String?,
    @SerializedName("scheduleStatus") val scheduleStatus: String?,
    @SerializedName("isGatheringEvent") val isGatheringEvent: Boolean?,
    @SerializedName("gatheringEventType") val gatheringEventType: String?,
    @SerializedName("cancelled") val cancelled: Boolean = false
)

data class SessionScheduleActivityDto(
    @SerializedName("activityId") val activityId: String?,
    @SerializedName("template") val template: SessionScheduleTemplateDto?,
    @SerializedName("override") val override: SessionScheduleOverrideDto?,
    @SerializedName("effective") val effective: SessionScheduleEffectiveDto?,
    @SerializedName("sourceLabel") val sourceLabel: String?
)

data class SessionScheduleDayDto(
    @SerializedName("dayNumber") val dayNumber: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("activities") val activities: List<SessionScheduleActivityDto>?
)

data class SessionScheduleViewDto(
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("tourId") val tourId: String?,
    @SerializedName("days") val days: List<SessionScheduleDayDto>?
)

data class SessionActivitySchedulePatchRequest(
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("startAt") val startAt: String? = null,
    @SerializedName("endAt") val endAt: String? = null,
    @SerializedName("locationName") val locationName: String? = null,
    @SerializedName("locationAddress") val locationAddress: String? = null,
    @SerializedName("isGatheringEvent") val isGatheringEvent: Boolean? = null,
    @SerializedName("gatheringEventType") val gatheringEventType: String? = null,
    @SerializedName("scheduleStatus") val scheduleStatus: String? = null,
    @SerializedName("operationalNote") val operationalNote: String? = null
)

data class ApiResponseGuideSessionList(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<GuideSessionSummaryDto>?
)

data class ApiResponseGuideSessionDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: GuideSessionDetailDto?
)

data class ApiResponseGuideSessionGuests(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: GuideSessionGuestsDto?
)

data class ApiResponseSessionSchedule(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: SessionScheduleViewDto?
)

data class ApiResponseSessionCheckin(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: SessionCheckinResultDto?
)

data class ApiResponseSessionParticipant(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: SessionParticipantResultDto?
)

data class ApiResponseParticipantActivity(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ParticipantActivityAttendanceResultDto?
)

data class GuideSessionExpenseDto(
    @SerializedName("id") val id: String,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("amount") val amount: Long?,
    @SerializedName("status") val status: String?,
    @SerializedName("expenseDate") val expenseDate: String?,
    @SerializedName("adminNote") val adminNote: String?
)

data class CreateGuideSessionExpenseRequest(
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: Long
)

data class ApiResponseGuideExpenseList(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<GuideSessionExpenseDto>?
)

data class ApiResponseGuideExpense(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: GuideSessionExpenseDto?
)
