package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseListPromotion
import retrofit2.Response
import retrofit2.http.GET

interface PromotionApiService {
    @GET("promotions/active")
    suspend fun getActivePromotions(): Response<ApiResponseListPromotion>
}
