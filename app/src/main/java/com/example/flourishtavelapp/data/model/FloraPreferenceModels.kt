package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

data class TravelPreferencesDto(
    @SerializedName("travelStyles") val travelStyles: List<String>? = emptyList(),
    @SerializedName("budgetLevel") val budgetLevel: String? = null,
    @SerializedName("favoriteDestinations") val favoriteDestinations: List<String>? = emptyList(),
    @SerializedName("favoriteFoods") val favoriteFoods: List<String>? = emptyList(),
    @SerializedName("foodDislikes") val foodDislikes: List<String>? = emptyList(),
    @SerializedName("foodAllergies") val foodAllergies: List<String>? = emptyList(),
    @SerializedName("preferredActivities") val preferredActivities: List<String>? = emptyList(),
    @SerializedName("avoidedActivities") val avoidedActivities: List<String>? = emptyList(),
    @SerializedName("travelPace") val travelPace: String? = null,
    @SerializedName("travelingWithChildren") val travelingWithChildren: Boolean? = null,
    @SerializedName("travelingWithElderly") val travelingWithElderly: Boolean? = null,
    @SerializedName("notificationConsent") val notificationConsent: Boolean? = null,
    @SerializedName("locationConsent") val locationConsent: Boolean? = null,
    @SerializedName("personalizationConsent") val personalizationConsent: Boolean? = null
)

data class UpdateTravelPreferencesRequest(
    @SerializedName("travelStyles") val travelStyles: List<String>? = null,
    @SerializedName("budgetLevel") val budgetLevel: String? = null,
    @SerializedName("favoriteDestinations") val favoriteDestinations: List<String>? = null,
    @SerializedName("favoriteFoods") val favoriteFoods: List<String>? = null,
    @SerializedName("foodDislikes") val foodDislikes: List<String>? = null,
    @SerializedName("foodAllergies") val foodAllergies: List<String>? = null,
    @SerializedName("preferredActivities") val preferredActivities: List<String>? = null,
    @SerializedName("avoidedActivities") val avoidedActivities: List<String>? = null,
    @SerializedName("travelPace") val travelPace: String? = null,
    @SerializedName("travelingWithChildren") val travelingWithChildren: Boolean? = null,
    @SerializedName("travelingWithElderly") val travelingWithElderly: Boolean? = null,
    @SerializedName("notificationConsent") val notificationConsent: Boolean? = null,
    @SerializedName("locationConsent") val locationConsent: Boolean? = null,
    @SerializedName("personalizationConsent") val personalizationConsent: Boolean? = null
)

data class FloraPreferencesApiResponse(
    val success: Boolean,
    val message: String?,
    val data: TravelPreferencesDto?
)

data class FloraFeedbackTagDto(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("category") val category: String,
    @SerializedName("suggestedPreferenceField") val suggestedPreferenceField: String? = null,
    @SerializedName("suggestedValue") val suggestedValue: String? = null
)

data class FloraExistingFeedbackDto(
    @SerializedName("rating") val rating: Int? = null,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("feedbackTags") val feedbackTags: List<String>? = emptyList()
)

data class FloraPostTourFeedbackContextDto(
    @SerializedName("bookingId") val bookingId: String? = null,
    @SerializedName("eligible") val eligible: Boolean = false,
    @SerializedName("alreadySubmitted") val alreadySubmitted: Boolean = false,
    @SerializedName("tourName") val tourName: String? = null,
    @SerializedName("completedAt") val completedAt: String? = null,
    @SerializedName("personalizationEnabled") val personalizationEnabled: Boolean = false,
    @SerializedName("availableTags") val availableTags: List<FloraFeedbackTagDto>? = emptyList(),
    @SerializedName("existingFeedback") val existingFeedback: FloraExistingFeedbackDto? = null
)

data class FloraPostTourFeedbackApiResponse(
    val success: Boolean,
    val message: String?,
    val data: FloraPostTourFeedbackContextDto?
)

data class FloraPreferencePreviewRequest(
    @SerializedName("selectedTagIds") val selectedTagIds: List<String>
)

data class FloraPreferenceChangeDto(
    @SerializedName("field") val field: String,
    @SerializedName("before") val before: String? = null,
    @SerializedName("after") val after: String? = null
)

data class FloraPreferencePreviewDto(
    @SerializedName("changes") val changes: List<FloraPreferenceChangeDto>? = emptyList(),
    @SerializedName("mergedPreview") val mergedPreview: TravelPreferencesDto? = null,
    @SerializedName("patchRequest") val patchRequest: UpdateTravelPreferencesRequest? = null
)

data class FloraPreferencePreviewApiResponse(
    val success: Boolean,
    val message: String?,
    val data: FloraPreferencePreviewDto?
)

data class CreateReviewRequest(
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("feedbackTags") val feedbackTags: List<String>? = null
)

data class CreateReviewApiResponse(
    val success: Boolean,
    val message: String?
)
