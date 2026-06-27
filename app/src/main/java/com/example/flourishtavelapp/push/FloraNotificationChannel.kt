package com.example.flourishtravelapp.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.flourishtravelapp.R

object FloraNotificationChannel {

    const val CHANNEL_ID = "flora_journey_reminders"
    const val CHANNEL_NAME = "Flora – Nhắc hành trình"

    fun ensure(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Thông báo nhắc hành trình từ Flora AI"
            enableVibration(false)
        }
        manager.createNotificationChannel(channel)
    }
}

object PushNotificationHelper {

    fun showFloraNotification(context: Context, title: String, body: String, notificationId: String?) {
        FloraNotificationChannel.ensure(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifId = (notificationId?.hashCode() ?: title.hashCode()) and 0x7fffffff
        val notification = NotificationCompat.Builder(context, FloraNotificationChannel.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_new)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        manager.notify(notifId, notification)
    }
}
