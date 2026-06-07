package com.example.flourishtavelapp.data.model

import com.google.gson.annotations.SerializedName

data class CreateBookingRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("guestCount") val guestCount: Int,
    @SerializedName("specialRequests") val specialRequests: String?, 
    @SerializedName("promotionCode") val promotionCode: String?,
    @SerializedName("contactPhone") val contactPhone: String?,
    @SerializedName("pickupAddress") val pickupAddress: String?,
    @SerializedName("guestNames") val guestNames: List<String>?,
    @SerializedName("guests") val guests: List<GuestItem>?,
    @SerializedName("emergencyContactName") val emergencyContactName: String?,
    @SerializedName("emergencyContactPhone") val emergencyContactPhone: String?,
    @SerializedName("paymentMethod") val paymentMethod: String?
)

data class GuestItem(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("idNumber") val idNumber: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String
)

data class CreateBookingResponse(
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("orderId") val orderId: String,
    @SerializedName("paymentUrl") val paymentUrl: String?,
    @SerializedName("expiresInSeconds") val expiresInSeconds: Int
)

data class ApiResponseCreateBooking(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: CreateBookingResponse?
)

data class ValidateSessionRequest(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("guestCount") val guestCount: Int,
    @SerializedName("tourId") val tourId: String? = null
)

data class ValidateSessionResult(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("message") val message: String?
)

data class ApiResponseValidateSession(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ValidateSessionResult?
)

data class ValidatePromoRequest(
    @SerializedName("code") val code: String,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("guestCount") val guestCount: Int?
)

data class ValidatePromoResult(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("discountAmount") val discountAmount: Double?,
    @SerializedName("message") val message: String?
)

data class ApiResponseValidatePromo(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ValidatePromoResult?
)

data class BookingSummary(
    @SerializedName("id") val id: String,
    @SerializedName("tourName") val tourName: String?,
    @SerializedName("status") val status: String,
    @SerializedName("totalAmount") val totalAmount: Long?,
    @SerializedName("createdAt") val createdAt: String?
)

data class ApiResponseBookingList(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<BookingSummary>?
)


