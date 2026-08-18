package com.example.flourishtravelapp.location

/**
 * Cửa sổ chia sẻ GPS với HDV: chỉ các ngày tour, đơn đã thanh toán.
 */
object TripLiveWindow {

    fun isActiveJourney(journeyStatus: String?): Boolean =
        journeyStatus.equals("ACTIVE", ignoreCase = true)

    fun isOngoingByDates(
        sessionStartDate: String?,
        sessionEndDate: String?,
        bookingStatus: String?,
        todayIsoVn: String
    ): Boolean {
        val st = bookingStatus?.lowercase().orEmpty()
        if (st !in setOf("paid", "confirmed", "completed")) return false
        if (sessionStartDate.isNullOrBlank()) return false
        val start = sessionStartDate.take(10)
        val end = (sessionEndDate ?: sessionStartDate).take(10)
        return todayIsoVn >= start && todayIsoVn <= end
    }
}
