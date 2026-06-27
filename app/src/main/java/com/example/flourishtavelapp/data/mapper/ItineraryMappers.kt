package com.example.flourishtravelapp.data.mapper

import com.example.flourishtravelapp.R
import com.example.flourishtravelapp.data.model.ActivityRef
import com.example.flourishtravelapp.data.model.ItineraryRef
import com.example.flourishtravelapp.data.model.TourDetailDto
import com.example.flourishtravelapp.ui.screens.ActivityItem
import com.example.flourishtravelapp.ui.screens.ActivityStatus
import com.example.flourishtravelapp.ui.screens.DayPlan

fun TourDetailDto.toDayPlans(): List<DayPlan> {
    return itineraries
        ?.sortedBy { it.dayNumber }
        ?.map { it.toDayPlan() }
        .orEmpty()
}

private fun ItineraryRef.toDayPlan(): DayPlan {
    val subtitle = summary?.takeIf { it.isNotBlank() }
        ?: description?.takeIf { it.isNotBlank() }
        .orEmpty()
    return DayPlan(
        dayNumber = dayNumber,
        dayTitle = "Ngày $dayNumber: $title",
        daySubtitle = subtitle,
        activities = activities.orEmpty().sortedBy { it.sortOrder }.map { it.toActivityItem() }
    )
}

private fun ActivityRef.toActivityItem(): ActivityItem = ActivityItem(
    id = id,
    title = title,
    status = ActivityStatus.Upcoming,
    description = description.orEmpty(),
    imageRes = fallbackImageForActivity(title)
)

fun fallbackImageForActivity(title: String): Int = when {
    title.contains("Phi Phi", ignoreCase = true) -> R.drawable.phiphi_bg
    title.contains("Maya", ignoreCase = true) -> R.drawable.maya_bg
    title.contains("Wat", ignoreCase = true) || title.contains("Arun", ignoreCase = true) -> R.drawable.awat_bg
    title.contains("Chao", ignoreCase = true) || title.contains("sông", ignoreCase = true) -> R.drawable.chaoriver_bg
    title.contains("Chiang Mai", ignoreCase = true) -> R.drawable.chiangmai_bg
    title.contains("Bangkok", ignoreCase = true) -> R.drawable.bangkook_bg
    else -> R.drawable.travel_bg
}
