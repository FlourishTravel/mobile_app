package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.data.model.ApiResponseChatbotResponse
import com.example.flourishtavelapp.data.model.ChatbotRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApiService {
    @POST("chatbot/message")
    suspend fun sendMessage(@Body request: ChatbotRequest): Response<ApiResponseChatbotResponse>
}
