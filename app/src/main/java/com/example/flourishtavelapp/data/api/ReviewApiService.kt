package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseListReview
import com.example.flourishtravelapp.data.model.CreateReviewApiResponse
import com.example.flourishtravelapp.data.model.CreateReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewApiService {
    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewRequest): Response<CreateReviewApiResponse>

    @GET("reviews/featured")
    suspend fun getFeaturedReviews(): Response<ApiResponseListReview>

    @GET("reviews/public")
    suspend fun getPublicReviews(): Response<ApiResponseListReview>

    @GET("reviews/me")
    suspend fun getMyReviews(): Response<ApiResponseListReview>
}
