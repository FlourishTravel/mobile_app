package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class ChatMemberViewDto(
    @SerializedName("userId") val userId: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("flora") val flora: Boolean = false
)

data class ChatReplyPreviewDto(
    @SerializedName("id") val id: String?,
    @SerializedName("senderId") val senderId: String?,
    @SerializedName("senderName") val senderName: String?,
    @SerializedName("content") val content: String?
)

data class ChatReactionSummaryDto(
    @SerializedName("type") val type: String?,
    @SerializedName("count") val count: Long = 0,
    @SerializedName("reactedByMe") val reactedByMe: Boolean = false
)

data class TourChatContextDto(
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("roomId") val roomId: String?,
    @SerializedName("roomName") val roomName: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("sessionStartDate") val sessionStartDate: String?,
    @SerializedName("sessionEndDate") val sessionEndDate: String?,
    @SerializedName("bookingStatus") val bookingStatus: String?,
    @SerializedName("guideName") val guideName: String?,
    @SerializedName("guideAvatarUrl") val guideAvatarUrl: String?,
    @SerializedName("canChat") val canChat: Boolean = false,
    @SerializedName("denyReason") val denyReason: String?,
    @SerializedName("members") val members: List<ChatMemberViewDto>? = emptyList()
)

data class ChatMessageViewDto(
    @SerializedName("id") val id: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("messageType") val messageType: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("senderId") val senderId: String?,
    @SerializedName("senderName") val senderName: String?,
    @SerializedName("senderAvatarUrl") val senderAvatarUrl: String?,
    @SerializedName("senderRole") val senderRole: String?,
    @SerializedName("isPinned") val isPinned: Boolean? = false,
    @SerializedName("replyTo") val replyTo: ChatReplyPreviewDto? = null,
    @SerializedName("reactions") val reactions: List<ChatReactionSummaryDto>? = emptyList()
)

data class SendChatMessageRequest(
    @SerializedName("content") val content: String,
    @SerializedName("replyToMessageId") val replyToMessageId: String? = null
)

data class ToggleChatReactionRequest(
    @SerializedName("reactionType") val reactionType: String
)

data class ApiResponseTourChatContext(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: TourChatContextDto?
)

data class ApiResponseChatMessageList(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<ChatMessageViewDto>?
)

data class ApiResponseChatMessage(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ChatMessageViewDto?
)
