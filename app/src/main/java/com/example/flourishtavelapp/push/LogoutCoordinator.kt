package com.example.flourishtravelapp.push

import android.content.Context
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LogoutCoordinator {

    suspend fun performLogout(context: Context, sessionManager: SessionManager) {
        withContext(Dispatchers.IO) {
            PushTokenRepository(context, sessionManager).unregisterCurrentToken()
            try {
                RetrofitClient.authApiService.logout()
            } catch (_: Exception) {
                // Clear local session even when server logout fails
            }
            sessionManager.clearSession()
        }
    }
}
