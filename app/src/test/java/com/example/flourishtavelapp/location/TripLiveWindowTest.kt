package com.example.flourishtravelapp.location

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TripLiveWindowTest {

    @Test
    fun dates_onlyDuringInclusiveTripWindow() {
        assertFalse(
            TripLiveWindow.isOngoingByDates("2026-08-18", "2026-08-20", "paid", "2026-08-17")
        )
        assertTrue(
            TripLiveWindow.isOngoingByDates("2026-08-18", "2026-08-20", "paid", "2026-08-18")
        )
        assertTrue(
            TripLiveWindow.isOngoingByDates("2026-08-18", "2026-08-20", "confirmed", "2026-08-20")
        )
        assertFalse(
            TripLiveWindow.isOngoingByDates("2026-08-18", "2026-08-20", "paid", "2026-08-21")
        )
    }

    @Test
    fun pendingBooking_neverShares() {
        assertFalse(
            TripLiveWindow.isOngoingByDates("2026-08-18", "2026-08-20", "pending", "2026-08-18")
        )
    }

    @Test
    fun journeyStatus_activeOnly() {
        assertTrue(TripLiveWindow.isActiveJourney("ACTIVE"))
        assertFalse(TripLiveWindow.isActiveJourney("UPCOMING"))
        assertFalse(TripLiveWindow.isActiveJourney("COMPLETED"))
        assertFalse(TripLiveWindow.isActiveJourney(null))
    }
}
