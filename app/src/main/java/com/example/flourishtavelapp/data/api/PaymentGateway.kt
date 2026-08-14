package com.example.flourishtravelapp.data.api

/**
 * Map UI payment labels to BE paymentMethod, and resolve a checkout URL
 * (create-booking payUrl, or resume PayOS / MoMo).
 */
object PaymentGateway {
    const val UI_PAYOS = "PayOS"
    const val UI_MOMO = "Online Payment"
    const val UI_BANK = "Bank Transfer"

    fun toApiMethod(uiLabel: String): String = when (uiLabel) {
        UI_PAYOS -> "payos"
        UI_MOMO -> "ewallet"
        else -> "bank"
    }

    fun isGateway(uiLabel: String): Boolean =
        uiLabel == UI_PAYOS || uiLabel == UI_MOMO

    suspend fun resolveCheckoutUrl(
        bookingId: String,
        createdUrl: String?,
        apiMethod: String
    ): String? {
        if (!createdUrl.isNullOrBlank() && !createdUrl.contains("/checkout/result")) {
            return createdUrl
        }
        val service = RetrofitClient.bookingApiService
        if (apiMethod == "payos") {
            val r = service.getPayOSPaymentUrl(bookingId)
            val url = r.body()?.data?.paymentUrl
            if (r.isSuccessful && !url.isNullOrBlank()) return url
        }
        if (apiMethod == "ewallet") {
            val r = service.getMomoPaymentUrl(bookingId)
            val url = r.body()?.data?.paymentUrl
            if (r.isSuccessful && !url.isNullOrBlank()) return url
        }
        if (createdUrl.isNullOrBlank()) {
            val payos = service.getPayOSPaymentUrl(bookingId)
            val payosUrl = payos.body()?.data?.paymentUrl
            if (payos.isSuccessful && !payosUrl.isNullOrBlank()) return payosUrl
            val momo = service.getMomoPaymentUrl(bookingId)
            val momoUrl = momo.body()?.data?.paymentUrl
            if (momo.isSuccessful && !momoUrl.isNullOrBlank()) return momoUrl
        }
        return createdUrl
    }
}
