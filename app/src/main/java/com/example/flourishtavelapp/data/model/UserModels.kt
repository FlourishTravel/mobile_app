package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

// GET /users/me response
data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("role") val role: String
)

data class ApiResponseUserProfile(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: UserProfileResponse?
)

// PATCH /users/me request  
data class UpdateProfileRequest(
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?
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
