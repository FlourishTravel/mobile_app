package com.example.flourishtravelapp.push

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FloraFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val notificationId = data["notificationId"]
        val title = message.notification?.title
            ?: "Flora có một cập nhật mới"
        val body = message.notification?.body
            ?: "Mở Flourish-Travel để xem thông tin hành trình mới nhất."
        PushNotificationHelper.showFloraNotification(this, title, body, notificationId)
    }

    override fun onNewToken(token: String) {
        val sessionManager = com.example.flourishtravelapp.data.session.SessionManager(this)
        if (!sessionManager.isLoggedIn()) return
        sessionManager.saveFcmToken(token)
        // Token sync runs on next settings open or explicit sync — avoids background network without consent check
    }
}
