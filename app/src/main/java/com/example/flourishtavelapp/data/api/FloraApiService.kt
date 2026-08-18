package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.FloraJourneyResponse
import com.example.flourishtravelapp.data.model.FloraLocationApiResponse
import com.example.flourishtravelapp.data.model.FloraLocationRequest
import com.example.flourishtravelapp.data.model.FloraNearbyApiResponse
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationRequest
import com.example.flourishtravelapp.data.model.FloraPostTourFeedbackApiResponse
import com.example.flourishtravelapp.data.model.FloraPreferencePreviewApiResponse
import com.example.flourishtravelapp.data.model.FloraPreferencePreviewRequest
import com.example.flourishtravelapp.data.model.FloraPreferencesApiResponse
import com.example.flourishtravelapp.data.model.UpdateTravelPreferencesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FloraApiService {
    @GET("flora/bookings/{bookingId}/journey")
    suspend fun getJourney(@Path("bookingId") bookingId: String): Response<FloraJourneyResponse>

    @POST("flora/bookings/{bookingId}/location")
    suspend fun postLocation(
        @Path("bookingId") bookingId: String,
        @Body body: FloraLocationRequest
    ): Response<FloraLocationApiResponse>

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
