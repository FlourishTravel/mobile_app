package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseChatbotResponse
import com.example.flourishtravelapp.data.model.ChatbotRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApiService {
    @POST("chatbot/message")
    suspend fun sendMessage(@Body request: ChatbotRequest): Response<ApiResponseChatbotResponse>
}
