package com.example.flourishtavelapp.push

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Ensures POST_NOTIFICATIONS is declared but no startup permission request exists in MainActivity.
 */
class DevicePushStartupPolicyTest {

    @Test
    fun manifest_declaresPostNotificationsOnly() {
        val manifest = File("src/main/AndroidManifest.xml")
        val text = manifest.readText()
        assertTrue(text.contains("POST_NOTIFICATIONS"))
        assertFalse(text.contains("RECEIVE_BOOT_COMPLETED"))
    }

    @Test
    fun mainActivity_doesNotRequestNotificationPermissionOnCreate() {
        val main = File("src/main/java/com/example/flourishtavelapp/MainActivity.kt")
        val text = main.readText()
        assertFalse(text.contains("POST_NOTIFICATIONS"))
        assertFalse(text.contains("RequestPermission"))
    }
}
