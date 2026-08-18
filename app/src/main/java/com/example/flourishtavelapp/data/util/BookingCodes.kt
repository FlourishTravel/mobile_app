package com.example.flourishtravelapp.data.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object BookingCodes {
    private val codeRegex = Regex("ft-[0-9a-f]{8}", RegexOption.IGNORE_CASE)

    fun fromBookingId(id: String?): String {
        val hex = id.orEmpty().replace("-", "").take(8)
        return if (hex.length == 8) "FT-${hex.uppercase()}" else ""
    }

    fun qrPayload(bookingCode: String?, bookingId: String?): String {
        val code = bookingCode?.trim().orEmpty()
        if (codeRegex.matches(code)) return "FT-" + code.substring(3).uppercase()
        return fromBookingId(bookingId)
    }

    fun parseQr(raw: String?): String? {
        val s = raw?.trim().orEmpty()
        if (s.isBlank()) return null
        codeRegex.find(s)?.value?.let { return "FT-" + it.substring(3).uppercase() }
        return null
    }
}

object BookingQrBitmaps {
    fun encode(payload: String, size: Int = 512): Bitmap? {
        if (payload.isBlank()) return null
        return try {
            val hints = mapOf(
                EncodeHintType.MARGIN to 1,
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.CHARACTER_SET to "UTF-8",
            )
            val matrix = QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, size, size, hints)
            val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bmp.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (_: Exception) {
            null
        }
    }
}
