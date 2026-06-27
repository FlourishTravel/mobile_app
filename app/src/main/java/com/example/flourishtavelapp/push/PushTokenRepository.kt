package com.example.flourishtravelapp.push

import android.content.Context
import com.example.flourishtravelapp.BuildConfig
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.PushDeviceRegisterRequest
import com.example.flourishtravelapp.data.model.PushDeviceUnregisterRequest
import com.example.flourishtravelapp.data.session.SessionManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class PushTokenRepository(
    private val context: Context,
    private val sessionManager: SessionManager
) {

    suspend fun syncIfEligible(notificationConsent: Boolean, androidPermissionGranted: Boolean): Boolean {
        if (!sessionManager.isLoggedIn()) return false
        if (!notificationConsent || !androidPermissionGranted) return false
        return registerCurrentToken(androidPermissionGranted)
    }

    suspend fun registerCurrentToken(androidPermissionGranted: Boolean): Boolean {
        if (!sessionManager.isLoggedIn()) return false
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            if (token.isBlank()) return false
            sessionManager.saveFcmToken(token)
            val response = RetrofitClient.pushDeviceApiService.register(
                PushDeviceRegisterRequest(
                    token = token,
                    platform = "ANDROID",
                    appVersion = BuildConfig.VERSION_NAME,
                    notificationPermissionGranted = androidPermissionGranted
                )
            )
            response.isSuccessful && response.body()?.success == true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun unregisterCurrentToken() {
        val token = sessionManager.getFcmToken() ?: return
        try {
            RetrofitClient.pushDeviceApiService.unregister(PushDeviceUnregisterRequest(token))
        } catch (_: Exception) {
            // Best effort — local session will still clear
        } finally {
            sessionManager.clearFcmToken()
        }
    }

    suspend fun fetchRemoteStatus(): PushDeviceStatus? {
        if (!sessionManager.isLoggedIn()) return null
        return try {
            val response = RetrofitClient.pushDeviceApiService.status()
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                PushDeviceStatus(
                    pushEnabled = data?.pushEnabled == true,
                    notificationConsent = data?.notificationConsent == true,
                    devicePermissionGranted = data?.devicePermissionGranted == true,
                    activeDeviceCount = data?.activeDeviceCount ?: 0
                )
            } else null
        } catch (_: Exception) {
            null
        }
    }

    data class PushDeviceStatus(
        val pushEnabled: Boolean,
        val notificationConsent: Boolean,
        val devicePermissionGranted: Boolean,
        val activeDeviceCount: Int
    )
}
