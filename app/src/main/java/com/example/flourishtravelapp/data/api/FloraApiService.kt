package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.FloraJourneyResponse
import com.example.flourishtravelapp.data.model.FloraNearbyApiResponse
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FloraApiService {
    @GET("flora/bookings/{bookingId}/journey")
    suspend fun getJourney(@Path("bookingId") bookingId: String): Response<FloraJourneyResponse>

    @POST("flora/bookings/{bookingId}/nearby-recommendations")
    suspend fun postNearbyRecommendations(
        @Path("bookingId") bookingId: String,
        @Body body: FloraNearbyRecommendationRequest
    ): Response<FloraNearbyApiResponse>
}
