package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseDestinationDetail
import com.example.flourishtravelapp.data.model.ApiResponseFestivalDetail
import com.example.flourishtravelapp.data.model.ApiResponseListDestination
import com.example.flourishtravelapp.data.model.ApiResponseListFestival
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DestinationApiService {
    @GET("destinations")
    suspend fun getDestinations(): Response<ApiResponseListDestination>

    @GET("destinations/{slug}")
    suspend fun getDestinationDetail(@Path("slug") slug: String): Response<ApiResponseDestinationDetail>

    @GET("destinations/festivals")
    suspend fun getFestivals(): Response<ApiResponseListFestival>

    @GET("destinations/festivals/{festivalSlug}")
    suspend fun getFestivalDetail(@Path("festivalSlug") festivalSlug: String): Response<ApiResponseFestivalDetail>
}
