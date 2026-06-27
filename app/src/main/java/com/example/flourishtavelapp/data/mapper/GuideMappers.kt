package com.example.flourishtravelapp.data.mapper

import com.example.flourishtravelapp.data.model.GuideActivityAttendanceDto
import com.example.flourishtravelapp.data.model.GuideGuestBookingRowDto
import com.example.flourishtravelapp.data.model.GuideItineraryDayDto
import com.example.flourishtravelapp.data.model.GuideItineraryStopDto
import com.example.flourishtravelapp.data.model.GuideParticipantAttendanceDto
import com.example.flourishtravelapp.data.model.GuideSessionDetailDto
import com.example.flourishtravelapp.data.model.GuideSessionGuestsDto
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.data.model.UserInfo
import com.example.flourishtravelapp.ui.screens.ActivityAttendance
import com.example.flourishtravelapp.ui.screens.GuideAccount
import com.example.flourishtravelapp.ui.screens.GuideTour
import com.example.flourishtravelapp.ui.screens.GuideTourDay
import com.example.flourishtravelapp.ui.screens.GuestBooking
import com.example.flourishtravelapp.ui.screens.GuestSessionData
import com.example.flourishtravelapp.ui.screens.ItineraryStop
import com.example.flourishtravelapp.ui.screens.ParticipantAttendance
import com.example.flourishtravelapp.ui.screens.PaymentStatus
import com.example.flourishtravelapp.ui.screens.TourCustomer
import com.example.flourishtravelapp.ui.screens.TourStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun UserInfo.toGuideAccount(totalTours: Int = 0): GuideAccount = GuideAccount(
    username = email,
    password = "",
    name = fullName,
    handle = "@${email.substringBefore("@")}",
    phone = phone.orEmpty(),
    rating = 4.9f,
    totalTours = totalTours,
    specialty = "Hướng dẫn viên Flourish"
)

fun GuideSessionSummaryDto.toGuideTour(): GuideTour = GuideTour(
    sessionId = sessionId,
    id = tourCode ?: sessionId.take(8).uppercase(),
    name = tourTitle.orEmpty(),
    destination = location.orEmpty(),
    startDate = formatGuideDate(startDate),
    endDate = formatGuideDate(endDate),
    startDateIso = startDate,
    endDateIso = endDate,
    durationDays = durationDaysBetween(startDate, endDate),
    totalCustomers = currentParticipants,
    checkedInParticipants = checkedInParticipants,
    status = mapGuideStatus(status),
    imageDescription = thumbnailUrl.orEmpty(),
    thumbnailUrl = thumbnailUrl,
    meetingPoint = "",
    itinerary = emptyList(),
    customers = emptyList()
)

fun GuideSessionDetailDto.toGuideTour(): GuideTour {
    val start = parseGuideDate(startDate)
    val today = LocalDate.now()
    val meeting = itineraryDays
        ?.flatMap { it.activities.orEmpty() }
        ?.firstOrNull { !it.locationName.isNullOrBlank() }
        ?.locationName
        .orEmpty()

    return GuideTour(
        sessionId = sessionId,
        id = tourCode ?: sessionId.take(8).uppercase(),
        name = tourTitle.orEmpty(),
        destination = location.orEmpty(),
        startDate = formatGuideDate(startDate),
        endDate = formatGuideDate(endDate),
        startDateIso = startDate,
        endDateIso = endDate,
        durationDays = durationDaysBetween(startDate, endDate),
        totalCustomers = currentParticipants,
        checkedInParticipants = checkedInParticipants,
        status = mapGuideStatus(status),
        imageDescription = thumbnailUrl.orEmpty(),
        thumbnailUrl = thumbnailUrl,
        meetingPoint = meeting,
        itinerary = itineraryDays.orEmpty().map { it.toGuideTourDay(start, today) },
        customers = emptyList()
    )
}

fun GuideSessionGuestsDto.toGuideTour(base: GuideTour): GuideTour = base.copy(
    customers = bookings.orEmpty().map { it.toTourCustomer() }
)

fun GuideSessionGuestsDto.toGuestSessionData(): GuestSessionData = GuestSessionData(
    sessionId = sessionId,
    tourTitle = tourTitle.orEmpty(),
    tourCode = tourCode.orEmpty(),
    startDate = startDate.orEmpty(),
    endDate = endDate.orEmpty(),
    totalGuestSlots = totalGuestSlots,
    checkedInGuestSlots = checkedInGuestSlots,
    checkedOutParticipants = checkedOutParticipants,
    paidBookingCount = paidBookingCount,
    bookingsWithSpecialRequests = bookingsWithSpecialRequests,
    itineraryStops = itineraryStops.orEmpty().map { it.toItineraryStop() },
    bookings = bookings.orEmpty().map { it.toGuestBooking() }
)

private fun GuideItineraryStopDto.toItineraryStop(): ItineraryStop = ItineraryStop(
    activityId = activityId.orEmpty(),
    dayNumber = dayNumber ?: 1,
    title = title.orEmpty(),
    locationName = locationName.orEmpty(),
    startTime = formatTimeHm(startTime),
    endTime = formatTimeHm(endTime),
    checkedInAtStopCount = checkedInAtStopCount
)

private fun GuideGuestBookingRowDto.toGuestBooking(): GuestBooking = GuestBooking(
    bookingId = bookingId,
    travelerUserId = travelerUserId,
    travelerName = travelerName.orEmpty(),
    phone = effectiveContactPhone?.takeIf { it.isNotBlank() } ?: phone.orEmpty(),
    email = email.orEmpty(),
    guestCount = guestCount.coerceAtLeast(1),
    specialRequests = specialRequests.orEmpty(),
    pickupAddress = pickupAddress.orEmpty(),
    emergencyContactName = emergencyContactName.orEmpty(),
    emergencyContactPhone = emergencyContactPhone.orEmpty(),
    checkedInGathering = checkedInGathering,
    allParticipantsCheckedIn = allParticipantsCheckedIn,
    participantAttendance = participantAttendance.orEmpty().map { it.toParticipantAttendance() }
)

private fun GuideParticipantAttendanceDto.toParticipantAttendance(): ParticipantAttendance =
    ParticipantAttendance(
        participantId = participantId.orEmpty(),
        displayName = displayName.orEmpty(),
        phone = phoneSnapshot.orEmpty(),
        role = participantRole.orEmpty(),
        lineIndex = lineIndex,
        checkInAt = checkInAt,
        checkOutAt = checkOutAt,
        activityAttendance = activityAttendance.orEmpty().map { it.toActivityAttendance() }
    )

private fun GuideActivityAttendanceDto.toActivityAttendance(): ActivityAttendance = ActivityAttendance(
    activityId = activityId.orEmpty(),
    checkInAt = checkInAt,
    checkOutAt = checkOutAt
)

private fun GuideItineraryDayDto.toGuideTourDay(
    tourStart: LocalDate?,
    today: LocalDate
): GuideTourDay {
    val dayNum = dayNumber ?: 1
    val dayDate = tourStart?.plusDays((dayNum - 1).toLong())
    val isCompleted = dayDate != null && dayDate.isBefore(today)
    val isCurrent = dayDate == today
    val desc = buildString {
        description?.takeIf { it.isNotBlank() }?.let { append(it) }
        val activityLines = activities.orEmpty()
            .mapNotNull { act ->
                act.title?.takeIf { it.isNotBlank() }?.let { title ->
                    act.description?.takeIf { it.isNotBlank() }?.let { "$title — $it" } ?: title
                }
            }
        if (activityLines.isNotEmpty()) {
            if (isNotEmpty()) append("\n\n")
            append(activityLines.joinToString("\n"))
        }
    }
    return GuideTourDay(
        day = dayNum,
        title = title.orEmpty(),
        description = desc.ifBlank { summary.orEmpty() },
        isCompleted = isCompleted,
        isCurrent = isCurrent
    )
}

private fun GuideGuestBookingRowDto.toTourCustomer(): TourCustomer = TourCustomer(
    id = bookingId,
    name = travelerName.orEmpty(),
    phone = effectiveContactPhone?.takeIf { it.isNotBlank() } ?: phone.orEmpty(),
    idCard = companions?.firstOrNull()?.maskedIdNumber.orEmpty(),
    gender = "",
    adultCount = guestCount.coerceAtLeast(1),
    childCount = 0,
    paymentStatus = PaymentStatus.PAID,
    note = specialRequests.orEmpty(),
    travelerUserId = travelerUserId,
    checkedInGathering = checkedInGathering,
    allParticipantsCheckedIn = allParticipantsCheckedIn,
    pickupAddress = pickupAddress.orEmpty(),
    emergencyContactName = emergencyContactName.orEmpty(),
    emergencyContactPhone = emergencyContactPhone.orEmpty(),
    participantAttendance = participantAttendance.orEmpty().map { it.toParticipantAttendance() }
)

fun mapGuideStatus(status: String?): TourStatus = when (status?.lowercase()) {
    "ongoing" -> TourStatus.ONGOING
    "upcoming" -> TourStatus.UPCOMING
    "completed", "cancelled" -> TourStatus.COMPLETED
    else -> TourStatus.UPCOMING
}

fun formatGuideDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return ""
    return try {
        val date = LocalDate.parse(isoDate.take(10))
        "%02d/%02d".format(date.dayOfMonth, date.monthValue)
    } catch (_: Exception) {
        isoDate.take(10)
    }
}

fun formatGuideDateRange(start: String?, end: String?): String {
    val s = formatGuideDate(start)
    val e = formatGuideDate(end)
    return when {
        s.isBlank() && e.isBlank() -> ""
        s.isBlank() -> e
        e.isBlank() -> s
        else -> "$s – $e"
    }
}

fun formatTimeHm(value: String?): String {
    if (value.isNullOrBlank()) return ""
    val s = value
    if (s.contains('T') || s.endsWith('Z')) {
        return try {
            java.time.Instant.parse(s)
                .atZone(java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        } catch (_: Exception) {
            s
        }
    }
    val m = Regex("""(\d{1,2}):(\d{2})""").find(s) ?: return s
    return "%02d:%s".format(m.groupValues[1].toInt(), m.groupValues[2])
}

fun formatDt(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        java.time.Instant.parse(iso)
            .atZone(java.time.ZoneId.systemDefault())
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (_: Exception) {
        iso.take(16).replace('T', ' ')
    }
}

fun parseGuideDate(isoDate: String?): LocalDate? = try {
    if (isoDate.isNullOrBlank()) null else LocalDate.parse(isoDate.take(10))
} catch (_: Exception) {
    null
}

fun durationDaysBetween(start: String?, end: String?): Int {
    val startDate = parseGuideDate(start) ?: return 1
    val endDate = parseGuideDate(end) ?: return 1
    return ChronoUnit.DAYS.between(startDate, endDate).toInt().coerceAtLeast(0) + 1
}

fun currentTourDayNumber(start: String?, end: String?): Int {
    val startDate = parseGuideDate(start) ?: return 1
    val endDate = parseGuideDate(end) ?: return 1
    val today = LocalDate.now()
    if (today.isBefore(startDate)) return 0
    if (today.isAfter(endDate)) return durationDaysBetween(start, end)
    return ChronoUnit.DAYS.between(startDate, today).toInt() + 1
}

fun attendanceAtActivity(participant: ParticipantAttendance, activityId: String): ActivityAttendance? =
    participant.activityAttendance.find { it.activityId == activityId }
