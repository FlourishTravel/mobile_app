package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

data class ChatbotRequest(
    @SerializedName("content") val content: String,
    @SerializedName("sessionId") val sessionId: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("state") val state: Map<String, Any>? = null
)

data class QuickReply(
    @SerializedName("label") val label: String,
    @SerializedName("payload") val payload: String? = null
)

data class ChatbotTourCard(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("price") val price: Long,
    @SerializedName("durationDays") val durationDays: Int,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("actions") val actions: List<QuickReply>?
)

data class ChatbotResponse(
    @SerializedName("reply") val reply: String,
    @SerializedName("tours") val tours: List<ChatbotTourCard>?,
    @SerializedName("quickReplies") val quickReplies: List<QuickReply>?,
    @SerializedName("state") val state: Map<String, Any>?
)

data class ApiResponseChatbotResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ChatbotResponse?
)
