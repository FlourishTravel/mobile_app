package com.example.flourishtravelapp.data.model

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
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("bookingStatus") val bookingStatus: String,
    @SerializedName("guestCount") val guestCount: Int? = null,
    @SerializedName("totalAmount") val totalAmount: Double? = null,
    @SerializedName("discountAmount") val discountAmount: Double? = null,
    @SerializedName("bookedAt") val bookedAt: String? = null,
    @SerializedName("sessionId") val sessionId: String? = null,
    @SerializedName("sessionStartDate") val sessionStartDate: String? = null,
    @SerializedName("sessionEndDate") val sessionEndDate: String? = null,
    @SerializedName("sessionStatus") val sessionStatus: String? = null,
    @SerializedName("tourId") val tourId: String? = null,
    @SerializedName("tourTitle") val tourTitle: String? = null,
    @SerializedName("tourSlug") val tourSlug: String? = null,
    @SerializedName("tourThumbnailUrl") val tourThumbnailUrl: String? = null,
    @SerializedName("tourDurationDays") val tourDurationDays: Int? = null,
    @SerializedName("tourDurationNights") val tourDurationNights: Int? = null,
    @SerializedName("categoryName") val categoryName: String? = null,
    @SerializedName("paymentStatus") val paymentStatus: String? = null,
    @SerializedName("paymentOrderId") val paymentOrderId: String? = null,
    @SerializedName("refundPending") val refundPending: Boolean = false
)

data class ApiResponseBookingList(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<BookingSummary>?
)

data class UserBookingGuestLineDto(
    @SerializedName("guestId") val guestId: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("maskedIdNumber") val maskedIdNumber: String?,
    @SerializedName("dateOfBirth") val dateOfBirth: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)

data class UserBookingDetailDto(
    @SerializedName("bookingId") val bookingId: String?,
    @SerializedName("bookingStatus") val bookingStatus: String?,
    @SerializedName("guestCount") val guestCount: Int?,
    @SerializedName("totalAmount") val totalAmount: Double?,
    @SerializedName("discountAmount") val discountAmount: Double?,
    @SerializedName("bookedAt") val bookedAt: String?,
    @SerializedName("sessionStartDate") val sessionStartDate: String?,
    @SerializedName("sessionEndDate") val sessionEndDate: String?,
    @SerializedName("tourId") val tourId: String?,
    @SerializedName("tourTitle") val tourTitle: String?,
    @SerializedName("tourThumbnailUrl") val tourThumbnailUrl: String?,
    @SerializedName("tourDurationDays") val tourDurationDays: Int?,
    @SerializedName("tourDurationNights") val tourDurationNights: Int?,
    @SerializedName("categoryName") val categoryName: String?,
    @SerializedName("customerEmail") val customerEmail: String?,
    @SerializedName("customerPhone") val customerPhone: String?,
    @SerializedName("paymentStatus") val paymentStatus: String?,
    @SerializedName("paymentOrderId") val paymentOrderId: String?,
    @SerializedName("refundPending") val refundPending: Boolean = false,
    @SerializedName("promotionCode") val promotionCode: String?,
    @SerializedName("specialRequests") val specialRequests: String?,
    @SerializedName("contactPhone") val contactPhone: String?,
    @SerializedName("pickupAddress") val pickupAddress: String?,
    @SerializedName("guestNames") val guestNames: String?,
    @SerializedName("emergencyContactName") val emergencyContactName: String?,
    @SerializedName("emergencyContactPhone") val emergencyContactPhone: String?,
    @SerializedName("guideName") val guideName: String?,
    @SerializedName("continuePaymentUrl") val continuePaymentUrl: String?,
    @SerializedName("guests") val guests: List<UserBookingGuestLineDto>?
)

data class ApiResponseBookingDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: UserBookingDetailDto?
)


