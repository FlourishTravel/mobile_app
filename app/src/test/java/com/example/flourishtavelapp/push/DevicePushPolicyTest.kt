package com.example.flourishtavelapp.push

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DevicePushPolicyTest {

    @Test
    fun consentDisabled_preventsRegistration() {
        assertFalse(DevicePushPolicy.canRegisterToken(notificationConsent = false, androidPermissionGranted = true))
    }

    @Test
    fun permissionDenied_preventsRegistration() {
        assertFalse(DevicePushPolicy.canRegisterToken(notificationConsent = true, androidPermissionGranted = false))
    }

    @Test
    fun consentAndPermission_allowsRegistration() {
        assertTrue(DevicePushPolicy.canRegisterToken(notificationConsent = true, androidPermissionGranted = true))
    }

    @Test
    fun permissionDeniedMessage_isCalmCopy() {
        assertTrue(DevicePushPolicy.PERMISSION_DENIED_MESSAGE.contains("trong ứng dụng"))
    }
}
