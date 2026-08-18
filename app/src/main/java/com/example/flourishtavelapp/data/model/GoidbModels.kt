package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null
)

data class AvailabilityCheckDto(
    @SerializedName("remainingSlots") val remainingSlots: Int? = null,
    @SerializedName("nextStartDate") val nextStartDate: String? = null,
    @SerializedName("tourTitle") val tourTitle: String? = null,
    @SerializedName("tourId") val tourId: String? = null,
    @SerializedName("sessionId") val sessionId: String? = null
)

data class CategoryRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("sortOrder") val sortOrder: Int? = null
)

data class ChatbotConfigImportDto(
    @SerializedName("chatbot_name") val chatbot_name: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("global_instructions") val global_instructions: String? = null,
    @SerializedName("intents") val intents: List<Any?>? = null,
    @SerializedName("intent_name") val intent_name: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("training_phrases") val training_phrases: List<String?>? = null,
    @SerializedName("entities_to_extract") val entities_to_extract: List<String?>? = null,
    @SerializedName("system_action") val system_action: Any? = null,
    @SerializedName("response_template") val response_template: String? = null,
    @SerializedName("context_output") val context_output: String? = null,
    @SerializedName("sentiment_analysis") val sentiment_analysis: String? = null,
    @SerializedName("sentiment_threshold") val sentiment_threshold: String? = null,
    @SerializedName("context_requirement") val context_requirement: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("api_endpoint") val api_endpoint: String? = null,
    @SerializedName("trigger") val trigger: String? = null,
    @SerializedName("priority") val priority: String? = null
)

data class ChatbotConfigResponseDto(
    @SerializedName("chatbotName") val chatbotName: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("globalInstructions") val globalInstructions: String? = null,
    @SerializedName("intents") val intents: List<Any?>? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("intentName") val intentName: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("entitiesToExtract") val entitiesToExtract: List<String?>? = null,
    @SerializedName("systemAction") val systemAction: Any? = null,
    @SerializedName("responseTemplate") val responseTemplate: String? = null,
    @SerializedName("contextOutput") val contextOutput: String? = null,
    @SerializedName("sentimentAnalysis") val sentimentAnalysis: String? = null,
    @SerializedName("sentimentThreshold") val sentimentThreshold: String? = null,
    @SerializedName("contextRequirement") val contextRequirement: String? = null,
    @SerializedName("sortOrder") val sortOrder: Int? = null,
    @SerializedName("trainingPhrases") val trainingPhrases: List<String?>? = null
)

data class ContactRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("tour") val tour: Tour? = null,
    @SerializedName("note") val note: String? = null
)

data class FloraLocationRequest(
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("accuracyMeters") val accuracyMeters: Double? = null,
    @SerializedName("capturedAt") val capturedAt: String? = null
)

data class FloraLocationResponse(
    @SerializedName("accepted") val accepted: Boolean? = null,
    @SerializedName("distanceToMeetingMeters") val distanceToMeetingMeters: Double? = null,
    @SerializedName("returnToBusSuggested") val returnToBusSuggested: Boolean? = null,
    @SerializedName("message") val message: String? = null
)

data class FloraTourRecommendDto(
    @SerializedName("message") val message: String? = null,
    @SerializedName("budgetVnd") val budgetVnd: Long? = null,
    @SerializedName("tourId") val tourId: String? = null,
    @SerializedName("tourTitle") val tourTitle: String? = null,
    @SerializedName("tourSlug") val tourSlug: String? = null,
    @SerializedName("durationLabel") val durationLabel: String? = null,
    @SerializedName("priceVnd") val priceVnd: Double? = null,
    @SerializedName("matchPercent") val matchPercent: Int? = null
)

data class FloraTourRecommendRequest(
    @SerializedName("budgetVnd") val budgetVnd: Long? = null,
    @SerializedName("destination") val destination: String? = null,
    @SerializedName("guests") val guests: Int? = null
)

data class GuideSessionMemberDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null
)

data class Message(
    @SerializedName("room") val room: ChatRoom? = null,
    @SerializedName("sender") val sender: User? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("fileUrl") val fileUrl: String? = null,
    @SerializedName("pinnedAt") val pinnedAt: String? = null,
    @SerializedName("pinnedBy") val pinnedBy: User? = null
)

data class ChatRoom(
    @SerializedName("session") val session: TourSession? = null,
    @SerializedName("roomName") val roomName: String? = null
)

data class TourSession(
    @SerializedName("tour") val tour: Tour? = null,
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("maxParticipants") val maxParticipants: Int? = null,
    @SerializedName("tourGuide") val tourGuide: User? = null
)

data class User(
    @SerializedName("email") val email: String? = null,
    @SerializedName("passwordHash") val passwordHash: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("nationality") val nationality: String? = null,
    @SerializedName("adminNote") val adminNote: String? = null,
    @SerializedName("lastLoginAt") val lastLoginAt: String? = null,
    @SerializedName("role") val role: Role? = null,
    @SerializedName("employeeCode") val employeeCode: String? = null,
    @SerializedName("jobTitle") val jobTitle: String? = null,
    @SerializedName("department") val department: String? = null
)

data class Role(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)

data class MessageReaction(
    @SerializedName("message") val message: Message? = null,
    @SerializedName("user") val user: User? = null,
    @SerializedName("reactionType") val reactionType: String? = null
)

data class NewsletterDto(
    val data: Any? = null
)
data class PublicGuideSummaryDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("jobTitle") val jobTitle: String? = null,
    @SerializedName("department") val department: String? = null,
    @SerializedName("languages") val languages: List<String?>? = null,
    @SerializedName("rating") val rating: Double? = null,
    @SerializedName("toursCompleted") val toursCompleted: Long? = null
)

data class Refund(
    @SerializedName("booking") val booking: Booking? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("reason") val reason: String? = null,
    @SerializedName("processedBy") val processedBy: User? = null,
    @SerializedName("processedAt") val processedAt: String? = null,
    @SerializedName("providerRefundId") val providerRefundId: String? = null
)

data class Booking(
    @SerializedName("user") val user: User? = null,
    @SerializedName("session") val session: TourSession? = null,
    @SerializedName("totalAmount") val totalAmount: Double? = null,
    @SerializedName("guestCount") val guestCount: Int? = null,
    @SerializedName("specialRequests") val specialRequests: String? = null,
    @SerializedName("contactPhone") val contactPhone: String? = null,
    @SerializedName("pickupAddress") val pickupAddress: String? = null,
    @SerializedName("guestNames") val guestNames: String? = null,
    @SerializedName("emergencyContactName") val emergencyContactName: String? = null,
    @SerializedName("emergencyContactPhone") val emergencyContactPhone: String? = null,
    @SerializedName("promotion") val promotion: Promotion? = null,
    @SerializedName("discountAmount") val discountAmount: Double? = null
)

data class TourRequest(
    @SerializedName("title") val title: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("basePrice") val basePrice: Double? = null,
    @SerializedName("durationDays") val durationDays: Int? = null,
    @SerializedName("durationNights") val durationNights: Int? = null,
    @SerializedName("categoryId") val categoryId: String? = null,
    @SerializedName("marketSegment") val marketSegment: String? = null,
    @SerializedName("destinationCity") val destinationCity: String? = null,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String? = null,
    @SerializedName("imageUrls") val imageUrls: List<String?>? = null,
    @SerializedName("videos") val videos: List<TourVideoRequest?>? = null
)

data class TourVideoRequest(
    @SerializedName("videoUrl") val videoUrl: String? = null,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("durationSeconds") val durationSeconds: Int? = null
)

data class TravelPreferenceDto(
    val data: Any? = null
)