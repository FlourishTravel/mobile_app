package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.data.model.ApiResponseListTour
import com.example.flourishtavelapp.data.model.ApiResponseVoid
import com.example.flourishtavelapp.data.model.FavoriteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {
    @GET("favorites")
    suspend fun getFavorites(): Response<ApiResponseListTour>

    @POST("favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): Response<ApiResponseVoid>

    @DELETE("favorites/{tourId}")
    suspend fun removeFavorite(@Path("tourId") tourId: String): Response<ApiResponseVoid>
}
