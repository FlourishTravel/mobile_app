package com.example.flourishtravelapp.ui.screens

// ─── Data Models ────────────────────────────────────────────────────────────

data class GuideAccount(
    val username: String,
    val password: String,
    val name: String,
    val handle: String,
    val phone: String,
    val rating: Float,
    val totalTours: Int,
    val specialty: String
)

data class GuideTourDay(
    val day: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class GuideTour(
    val sessionId: String,
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val startDateIso: String? = null,
    val endDateIso: String? = null,
    val durationDays: Int,
    val totalCustomers: Int,
    val checkedInParticipants: Int = 0,
    val status: TourStatus,
    val imageDescription: String,
    val thumbnailUrl: String? = null,
    val meetingPoint: String,
    val itinerary: List<GuideTourDay>,
    val customers: List<TourCustomer>
)

enum class TourStatus(val label: String, val color: Long) {
    ONGOING("Đang diễn ra", 0xFF00796B),
    UPCOMING("Sắp khởi hành", 0xFF1565C0),
    COMPLETED("Đã hoàn thành", 0xFF616161)
}

data class TourCustomer(
    val id: String,
    val name: String,
    val phone: String,
    val idCard: String,
    val gender: String,
    val adultCount: Int,
    val childCount: Int,
    val paymentStatus: PaymentStatus,
    val note: String = "",
    val travelerUserId: String? = null,
    val checkedInGathering: Boolean = false,
    val allParticipantsCheckedIn: Boolean = false,
    val pickupAddress: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val participantAttendance: List<ParticipantAttendance> = emptyList()
)

data class ParticipantAttendance(
    val participantId: String,
    val displayName: String,
    val phone: String,
    val role: String,
    val lineIndex: Int,
    val checkInAt: String?,
    val checkOutAt: String?,
    val activityAttendance: List<ActivityAttendance> = emptyList()
)

data class ActivityAttendance(
    val activityId: String,
    val checkInAt: String?,
    val checkOutAt: String?
)

data class ItineraryStop(
    val activityId: String,
    val dayNumber: Int,
    val title: String,
    val locationName: String,
    val startTime: String,
    val endTime: String,
    val checkedInAtStopCount: Int
)

data class GuestSessionData(
    val sessionId: String,
    val tourTitle: String,
    val tourCode: String,
    val startDate: String,
    val endDate: String,
    val totalGuestSlots: Int,
    val checkedInGuestSlots: Int,
    val checkedOutParticipants: Int,
    val paidBookingCount: Int,
    val bookingsWithSpecialRequests: Int,
    val itineraryStops: List<ItineraryStop>,
    val bookings: List<GuestBooking>
)

data class GuestBooking(
    val bookingId: String,
    val travelerUserId: String?,
    val travelerName: String,
    val phone: String,
    val email: String,
    val guestCount: Int,
    val specialRequests: String,
    val pickupAddress: String,
    val emergencyContactName: String,
    val emergencyContactPhone: String,
    val checkedInGathering: Boolean,
    val allParticipantsCheckedIn: Boolean,
    val participantAttendance: List<ParticipantAttendance>
)

enum class PaymentStatus(val label: String, val color: Long) {
    PAID("Đã thanh toán", 0xFF2E7D32),
    PENDING("Chờ xác nhận", 0xFFE65100),
    DEPOSIT("Đặt cọc 30%", 0xFF1565C0)
}

enum class ScheduleStatus(val label: String, val color: Long) {
    AVAILABLE("Có thể đi", 0xFF4CAF50),
    BOOKED("Đã book", 0xFF1565C0),
    ASSIGNED("Đã phân tour", 0xFF00796B),
    OFF("Nghỉ", 0xFF9E9E9E)
}

data class GuideScheduleDay(
    val day: Int,
    val month: Int,
    val year: Int,
    val status: ScheduleStatus = ScheduleStatus.AVAILABLE,
    val assignedTourName: String? = null
)
