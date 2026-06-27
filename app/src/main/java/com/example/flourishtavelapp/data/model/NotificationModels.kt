package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("body") val body: String?,
    @SerializedName("data") val data: String?,
    @SerializedName("isRead") val isRead: Boolean? = false,
    @SerializedName("readAt") val readAt: String?,
    @SerializedName("createdAt") val createdAt: String?
)

data class NotificationPageDto(
    @SerializedName("content") val content: List<NotificationDto>?,
    @SerializedName("totalElements") val totalElements: Long? = 0
)

data class ApiResponseNotificationPage(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: NotificationPageDto?
)

data class ApiResponseNotification(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: NotificationDto?
)
