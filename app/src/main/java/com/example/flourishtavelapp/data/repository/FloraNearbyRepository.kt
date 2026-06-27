package com.example.flourishtravelapp.data.repository

import com.example.flourishtravelapp.data.api.FloraApiService
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationRequest
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationResponse
import com.example.flourishtravelapp.location.NearbyLocationPolicy
import java.io.IOException

class FloraNearbyRepository(
    private val floraApiService: FloraApiService
) {
    suspend fun fetchNearby(
        bookingId: String,
        latitude: Double?,
        longitude: Double?
    ): NearbyFetchResult {
        if (!NearbyLocationPolicy.isValidBookingId(bookingId)) {
            return NearbyFetchResult.Error(NearbyLocationPolicy.invalidBookingMessage())
        }

        return try {
            val body = FloraNearbyRecommendationRequest(
                latitude = latitude,
                longitude = longitude
            )
            val response = floraApiService.postNearbyRecommendations(bookingId, body)
            when {
                response.isSuccessful && response.body()?.success == true -> {
                    val data = response.body()?.data
                    if (data?.recommendations.isNullOrEmpty() &&
                        data?.locationSource == "UNAVAILABLE"
                    ) {
                        NearbyFetchResult.Empty(data)
                    } else if (data?.recommendations.isNullOrEmpty()) {
                        NearbyFetchResult.Empty(data)
                    } else {
                        NearbyFetchResult.Success(data!!)
                    }
                }
                response.code() == 401 || response.code() == 403 || response.code() == 429 ->
                    NearbyFetchResult.Error(
                        NearbyLocationPolicy.apiErrorMessage(response.code(), networkFailure = false)
                    )
                else -> NearbyFetchResult.Error(
                    NearbyLocationPolicy.apiErrorMessage(response.code(), networkFailure = false)
                )
            }
        } catch (_: IOException) {
            NearbyFetchResult.Error(
                NearbyLocationPolicy.apiErrorMessage(httpCode = null, networkFailure = true)
            )
        } catch (_: Exception) {
            NearbyFetchResult.Error(
                NearbyLocationPolicy.apiErrorMessage(httpCode = null, networkFailure = true)
            )
        }
    }
}

sealed class NearbyFetchResult {
    data class Success(val data: FloraNearbyRecommendationResponse) : NearbyFetchResult()
    data class Empty(val data: FloraNearbyRecommendationResponse?) : NearbyFetchResult()
    data class Error(val message: String) : NearbyFetchResult()
}
