package com.example.flourishtravelapp.location

data class NearbyCoordinateDecision(
    val latitude: Double?,
    val longitude: Double?,
    val infoMessage: String?
)

object NearbyLocationPolicy {

    fun shouldRequestAndroidPermission(locationConsent: Boolean?): Boolean =
        locationConsent == true

    fun coordinatesForRequest(
        locationConsent: Boolean?,
        locationResult: MobileLocationResult?
    ): NearbyCoordinateDecision {
        if (locationConsent != true) {
            return NearbyCoordinateDecision(
                latitude = null,
                longitude = null,
                infoMessage = "Flora sẽ gợi ý theo địa điểm trong lịch trình vì bạn chưa bật chia sẻ vị trí."
            )
        }

        return when (locationResult) {
            is MobileLocationResult.Success -> NearbyCoordinateDecision(
                latitude = locationResult.latitude,
                longitude = locationResult.longitude,
                infoMessage = null
            )
            is MobileLocationResult.ServiceDisabled -> NearbyCoordinateDecision(
                null, null,
                "Flora chưa lấy được vị trí hiện tại, nên đang dùng địa điểm trong lịch trình."
            )
            is MobileLocationResult.Timeout,
            is MobileLocationResult.PermissionDenied,
            is MobileLocationResult.Unavailable,
            null -> NearbyCoordinateDecision(
                null, null,
                "Flora đang gợi ý theo địa điểm trong lịch trình của bạn."
            )
        }
    }

    fun locationSourceLabel(source: String?): String? = when (source) {
        "USER_LOCATION" -> "Gợi ý theo vị trí hiện tại của bạn"
        "ACTIVITY_LOCATION" -> "Gợi ý theo địa điểm trong lịch trình"
        "DESTINATION_FALLBACK" -> "Gợi ý theo khu vực điểm đến (dữ liệu dự kiến)"
        "UNAVAILABLE" -> "Flora chưa xác định được vị trí để gợi ý"
        else -> null
    }

    fun apiErrorMessage(httpCode: Int?, networkFailure: Boolean): String = when {
        networkFailure -> "Flora chưa tải được gợi ý. Bạn kiểm tra kết nối và thử lại nhé."
        httpCode == 401 -> "Bạn cần đăng nhập để Flora hỗ trợ theo chuyến đi của mình."
        httpCode == 403 -> "Flora chưa có quyền truy cập chuyến đi này."
        httpCode == 429 -> "Flora đang nhận khá nhiều yêu cầu. Bạn vui lòng thử lại sau ít phút nhé."
        else -> "Flora chưa tải được gợi ý gần đây. Bạn thử lại sau nhé."
    }

    fun emptyRecommendationsMessage(): String =
        "Flora chưa tìm được địa điểm phù hợp gần đây. Bạn có thể thử lại sau hoặc xem lịch trình tour."

    fun invalidBookingMessage(): String =
        "Flora chưa xác định được chuyến đi để đưa ra gợi ý gần đây."

    fun isValidBookingId(bookingId: String?): Boolean {
        if (bookingId.isNullOrBlank()) return false
        return UUID_REGEX.matches(bookingId)
    }

    private val UUID_REGEX =
        Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
}
