package com.example.flourishtavelapp.location

interface LocationProvider {
    suspend fun getForegroundLocation(timeoutMs: Long = DEFAULT_TIMEOUT_MS): MobileLocationResult

    companion object {
        const val DEFAULT_TIMEOUT_MS = 8_000L
    }
}
