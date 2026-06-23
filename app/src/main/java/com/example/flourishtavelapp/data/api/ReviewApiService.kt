package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.data.model.CreateReviewApiResponse
import com.example.flourishtavelapp.data.model.CreateReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewApiService {
    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewRequest): Response<CreateReviewApiResponse>
}
