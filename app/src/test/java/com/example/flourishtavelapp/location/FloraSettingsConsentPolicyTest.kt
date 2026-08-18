package com.example.flourishtravelapp.location

import org.junit.Assert.*
import org.junit.Test

/**
 * Flora nearby gợi ý vẫn cần consent + quyền Android.
 * GPS cho HDV trong ngày tour là luồng riêng (TripLiveLocationShare).
 */
class FloraSettingsConsentPolicyTest {

    @Test
    fun locationConsentOff_preventsNearbyGpsAttempt() {
        assertFalse(NearbyLocationPolicy.shouldRequestAndroidPermission(false))
        val decision = NearbyLocationPolicy.coordinatesForRequest(false, null)
        assertNull(decision.latitude)
        assertNull(decision.longitude)
    }

    @Test
    fun locationConsentOn_stillRequiresSeparateAndroidPermissionForGps() {
        assertTrue(NearbyLocationPolicy.shouldRequestAndroidPermission(true))
        val withoutAndroid = NearbyLocationPolicy.coordinatesForRequest(
            true,
            MobileLocationResult.PermissionDenied
        )
        assertNull(withoutAndroid.latitude)
    }

    @Test
    fun togglingConsentDoesNotImplyPermissionGranted() {
        val flourishOn = true
        val androidGranted = false
        val decision = NearbyLocationPolicy.coordinatesForRequest(
            flourishOn,
            if (androidGranted) MobileLocationResult.Success(1.0, 2.0)
            else MobileLocationResult.PermissionDenied
        )
        assertNull(decision.latitude)
    }

    @Test
    fun allergyLimitationWordingPresent() {
        val msg =
            "Thông tin này chỉ giúp Flora hạn chế gợi ý không phù hợp, không thay thế việc kiểm tra trực tiếp với nhà hàng."
        assertTrue(msg.contains("không thay thế"))
        assertFalse(msg.contains("đảm bảo"))
    }
}
