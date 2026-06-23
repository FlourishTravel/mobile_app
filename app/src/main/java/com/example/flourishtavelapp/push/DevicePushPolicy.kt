package com.example.flourishtavelapp.push

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object DevicePushPolicy {

    fun requiresRuntimePermission(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun hasNotificationPermission(context: Context): Boolean {
        if (!requiresRuntimePermission()) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun canRegisterToken(notificationConsent: Boolean, androidPermissionGranted: Boolean): Boolean {
        return notificationConsent && androidPermissionGranted
    }

    const val PERMISSION_DENIED_MESSAGE =
        "Flora vẫn hiển thị thông báo trong ứng dụng. Bạn có thể bật thông báo trên thiết bị trong phần cài đặt khi cần."
}
