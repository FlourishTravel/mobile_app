package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.data.model.FloraJourneyResponse
import com.example.flourishtavelapp.data.model.FloraNearbyApiResponse
import com.example.flourishtavelapp.data.model.FloraNearbyRecommendationRequest
import com.example.flourishtavelapp.data.model.FloraPostTourFeedbackApiResponse
import com.example.flourishtavelapp.data.model.FloraPreferencePreviewApiResponse
import com.example.flourishtavelapp.data.model.FloraPreferencePreviewRequest
import com.example.flourishtavelapp.data.model.FloraPreferencesApiResponse
import com.example.flourishtavelapp.data.model.UpdateTravelPreferencesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FloraApiService {
    @GET("flora/bookings/{bookingId}/journey")
    suspend fun getJourney(@Path("bookingId") bookingId: String): Response<FloraJourneyResponse>

    @GET("flora/preferences/me")
    suspend fun getPreferences(): Response<FloraPreferencesApiResponse>

    @PATCH("flora/preferences/me")
    suspend fun updatePreferences(
        @Body body: UpdateTravelPreferencesRequest
    ): Response<FloraPreferencesApiResponse>

    @POST("flora/bookings/{bookingId}/nearby-recommendations")
    suspend fun postNearbyRecommendations(
        @Path("bookingId") bookingId: String,
        @Body body: FloraNearbyRecommendationRequest
    ): Response<FloraNearbyApiResponse>

    @GET("flora/bookings/{bookingId}/post-tour-feedback")
    suspend fun getPostTourFeedback(
        @Path("bookingId") bookingId: String
    ): Response<FloraPostTourFeedbackApiResponse>

    @POST("flora/feedback/preference-preview")
    suspend fun previewFeedbackPreferences(
        @Body body: FloraPreferencePreviewRequest
    ): Response<FloraPreferencePreviewApiResponse>
}
