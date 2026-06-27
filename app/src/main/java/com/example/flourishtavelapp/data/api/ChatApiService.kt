package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseChatMessage
import com.example.flourishtravelapp.data.model.ApiResponseChatMessageList
import com.example.flourishtravelapp.data.model.ApiResponseTourChatContext
import com.example.flourishtravelapp.data.model.SendChatMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {
    @GET("chat/bookings/{bookingId}/context")
    suspend fun getTourChatContext(
        @Path("bookingId") bookingId: String
    ): Response<ApiResponseTourChatContext>

    @GET("chat/bookings/{bookingId}/messages")
    suspend fun getBookingChatMessages(
        @Path("bookingId") bookingId: String,
        @Query("limit") limit: Int? = 50
    ): Response<ApiResponseChatMessageList>

    @POST("chat/bookings/{bookingId}/messages")
    suspend fun sendBookingChatMessage(
        @Path("bookingId") bookingId: String,
        @Body body: SendChatMessageRequest
    ): Response<ApiResponseChatMessage>
}
