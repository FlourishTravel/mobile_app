package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class PushDeviceRegisterRequest(
    @SerializedName("token") val token: String,
    @SerializedName("platform") val platform: String = "ANDROID",
    @SerializedName("appVersion") val appVersion: String? = null,
    @SerializedName("notificationPermissionGranted") val notificationPermissionGranted: Boolean = false
)

data class PushDeviceUnregisterRequest(
    @SerializedName("token") val token: String
)

data class PushDeviceRegisterResponse(
    @SerializedName("registered") val registered: Boolean?,
    @SerializedName("pushEnabled") val pushEnabled: Boolean?,
    @SerializedName("devicePermissionGranted") val devicePermissionGranted: Boolean?
)

data class PushDeviceStatusResponse(
    @SerializedName("pushEnabled") val pushEnabled: Boolean?,
    @SerializedName("notificationConsent") val notificationConsent: Boolean?,
    @SerializedName("devicePermissionGranted") val devicePermissionGranted: Boolean?,
    @SerializedName("activeDeviceCount") val activeDeviceCount: Int?
)

data class ApiEnvelope<T>(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String?
)
