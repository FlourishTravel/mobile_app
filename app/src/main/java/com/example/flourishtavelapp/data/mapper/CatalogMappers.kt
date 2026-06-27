package com.example.flourishtravelapp.data.mapper

import com.example.flourishtravelapp.R
import com.example.flourishtravelapp.data.model.TicketCardDto
import com.example.flourishtravelapp.data.util.resolveMediaUrl
import com.example.flourishtravelapp.ui.screens.TravelActivity
import java.text.NumberFormat
import java.util.Locale

fun TicketCardDto.toTravelActivity(): TravelActivity {
    val priceText = priceLabel?.takeIf { it.isNotBlank() }
        ?: priceVnd?.let { formatVnd(it) }
        ?: "Liên hệ"

    return TravelActivity(
        id = id,
        slug = slug,
        title = name,
        location = destinationCity?.takeIf { it.isNotBlank() }
            ?: locationLabel?.takeIf { it.isNotBlank() }
            ?: routeLabel.orEmpty(),
        rating = rating?.toString() ?: "4.5",
        reviewCount = "—",
        bookedCount = if (featured == true) "Nổi bật" else "Mới",
        price = priceText,
        originalPrice = null,
        promoText = shortDescription?.takeIf { it.isNotBlank() },
        badge = when {
            featured == true -> "Nổi bật"
            eTicket == true -> "Vé điện tử"
            else -> null
        },
        imageUrl = resolveMediaUrl(imageUrl),
        imageRes = fallbackTicketImage(destinationCity ?: name),
        categoryId = category.orEmpty(),
        keyword = destinationCity ?: category.orEmpty(),
        showTimeLabel = showTimeLabel,
        routeLabel = routeLabel
    )
}

/** Map chip label → BE category param (giống web /activities). */
fun categoryLabelToApiParam(label: String): String? = when {
    label.equals("Tất cả", ignoreCase = true) -> null
    label.equals("Điểm tham quan", ignoreCase = true) -> "attraction"
    label.contains("Show", ignoreCase = true) || label.contains("vui chơi", ignoreCase = true) -> "show"
    label.equals("Di chuyển", ignoreCase = true) || label.equals("Transport", ignoreCase = true) -> "transport"
    label.equals("Visa", ignoreCase = true) -> "visa"
    else -> null
}

fun formatVnd(amount: Double): String {
    val formatted = NumberFormat.getNumberInstance(Locale("vi", "VN")).format(amount.toLong())
    return "$formatted đ"
}

private fun fallbackTicketImage(text: String): Int = when {
    text.contains("Bangkok", ignoreCase = true) -> R.drawable.bangkook_bg
    text.contains("Wat", ignoreCase = true) -> R.drawable.awat_bg
    text.contains("Phi Phi", ignoreCase = true) || text.contains("Maya", ignoreCase = true) -> R.drawable.maya_bg
    text.contains("Chiang Mai", ignoreCase = true) -> R.drawable.chiangmai_bg
    text.contains("Singapore", ignoreCase = true) -> R.drawable.travel_bg
    else -> R.drawable.travel_bg
}
