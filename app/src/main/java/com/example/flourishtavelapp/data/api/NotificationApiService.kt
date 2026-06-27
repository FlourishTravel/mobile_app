package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseNotification
import com.example.flourishtravelapp.data.model.ApiResponseNotificationPage
import com.example.flourishtravelapp.data.model.ApiResponseVoid
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @GET("notifications")
    suspend fun getNotifications(
        @Query("unread_only") unreadOnly: Boolean? = null,
        @Query("limit") limit: Int? = 50
    ): Response<ApiResponseNotificationPage>

    @PATCH("notifications/{id}/read")
    suspend fun markRead(@Path("id") id: String): Response<ApiResponseNotification>

    @POST("notifications/read-all")
    suspend fun markAllRead(): Response<ApiResponseVoid>
}
