package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseListTicketCard
import com.example.flourishtravelapp.data.model.ApiResponseTicketCard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {
    @GET("catalog/tickets")
    suspend fun getTickets(
        @Query("category") category: String? = null,
        @Query("destination") destination: String? = null
    ): Response<ApiResponseListTicketCard>

    @GET("catalog/tickets/{slug}")
    suspend fun getTicketBySlug(
        @Path("slug") slug: String
    ): Response<ApiResponseTicketCard>
}
