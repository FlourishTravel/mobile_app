package com.example.flourishtravelapp.data.model

/** BE dùng `TOUR_GUIDE`; một số client cũ dùng `GUIDE`. */
fun String?.isTourGuideRole(): Boolean {
    if (isNullOrBlank()) return false
    return equals("GUIDE", ignoreCase = true) || equals("TOUR_GUIDE", ignoreCase = true)
}
