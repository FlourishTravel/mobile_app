package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

// GET /users/me response
data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("role") val role: String,
    @SerializedName("jobTitle") val jobTitle: String? = null,
    @SerializedName("guideShortBio") val guideShortBio: String? = null,
    @SerializedName("guideBio") val guideBio: String? = null,
    @SerializedName("guideLanguages") val guideLanguages: List<String>? = null,
    @SerializedName("guideSpecialties") val guideSpecialties: List<String>? = null,
    @SerializedName("guideCoverUrl") val guideCoverUrl: String? = null,
    @SerializedName("guideExperienceYears") val guideExperienceYears: Int? = null,
    @SerializedName("guideBaseLocation") val guideBaseLocation: String? = null,
    @SerializedName("guideBadges") val guideBadges: List<String>? = null,
    @SerializedName("guideVerified") val guideVerified: Boolean? = null,
    @SerializedName("guidePublicApproved") val guidePublicApproved: Boolean? = null,
    @SerializedName("guidePendingReview") val guidePendingReview: Boolean? = null
)

data class ApiResponseUserProfile(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: UserProfileResponse?
)

// PATCH /users/me request  
data class UpdateProfileRequest(
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("guideShortBio") val guideShortBio: String? = null,
    @SerializedName("guideBio") val guideBio: String? = null,
    @SerializedName("guideLanguages") val guideLanguages: List<String>? = null,
    @SerializedName("guideSpecialties") val guideSpecialties: List<String>? = null,
    @SerializedName("guideCoverUrl") val guideCoverUrl: String? = null,
    @SerializedName("guideExperienceYears") val guideExperienceYears: Int? = null,
    @SerializedName("guideBaseLocation") val guideBaseLocation: String? = null
)

// POST /upload response
data class ApiResponseString(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: String? // URL of uploaded image
)

// Generic void API response
data class ApiResponseVoid(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Any?
)
