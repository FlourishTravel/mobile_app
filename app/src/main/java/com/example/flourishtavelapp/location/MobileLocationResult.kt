package com.example.flourishtravelapp.location

sealed class MobileLocationResult {
    data class Success(val latitude: Double, val longitude: Double) : MobileLocationResult()
    data object PermissionDenied : MobileLocationResult()
    data object ServiceDisabled : MobileLocationResult()
    data object Timeout : MobileLocationResult()
    data object Unavailable : MobileLocationResult()
}
