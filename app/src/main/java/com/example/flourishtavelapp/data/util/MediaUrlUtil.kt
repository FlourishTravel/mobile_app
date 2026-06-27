package com.example.flourishtravelapp.data.util

import com.example.flourishtravelapp.data.api.RetrofitClient

fun resolveMediaUrl(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://", ignoreCase = true) || path.startsWith("https://", ignoreCase = true)) {
        return path
    }
    val root = RetrofitClient.BASE_URL.substringBefore("/api/")
    return if (path.startsWith("/")) "$root$path" else "$root/$path"
}
