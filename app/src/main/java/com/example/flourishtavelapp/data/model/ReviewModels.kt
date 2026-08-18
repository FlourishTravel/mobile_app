package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

// GET /reviews/featured | /reviews/public | /reviews/me
data class ReviewView(
    @SerializedName("id") val id: String,
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("tourId") val tourId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("feedbackTags") val feedbackTags: String?,
    @SerializedName("isPublished") val isPublished: Boolean?,
    @SerializedName("isFeatured") val isFeatured: Boolean?,
    @SerializedName("createdAt") val createdAt: String?
)

data class ApiResponseListReview(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<ReviewView>?
)
