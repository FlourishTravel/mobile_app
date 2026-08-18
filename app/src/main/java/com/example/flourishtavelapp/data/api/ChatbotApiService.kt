package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseChatbotResponse
import com.example.flourishtravelapp.data.model.ApiResponseNearbyPlace
import com.example.flourishtravelapp.data.model.ApiResponseWeatherForecast
import com.example.flourishtravelapp.data.model.ChatbotRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatbotApiService {
    @POST("chatbot/message")
    suspend fun sendMessage(@Body request: ChatbotRequest): Response<ApiResponseChatbotResponse>

    @GET("chatbot/weather-forecast")
    suspend fun getWeatherForecast(
        @Query("destination") destination: String? = null
    ): Response<ApiResponseWeatherForecast>

    @GET("chatbot/nearby-places")
    suspend fun getNearbyPlaces(
        @Query("destination") destination: String? = null,
        @Query("poi_type") poiType: String? = null
    ): Response<ApiResponseNearbyPlace>
}
