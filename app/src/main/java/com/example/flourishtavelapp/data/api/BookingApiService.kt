package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import retrofit2.http.Query

interface BookingApiService {
    @POST("bookings")
    suspend fun createBooking(@Body request: CreateBookingRequest): Response<ApiResponseCreateBooking>
    
    @GET("bookings/me")
    suspend fun getMyBookings(): Response<ApiResponseBookingList>

    @GET("bookings/{id}")
    suspend fun getBookingDetail(@Path("id") id: String): Response<ApiResponseBookingDetail>
    
    @POST("bookings/{id}/cancel")
    suspend fun cancelBooking(@Path("id") id: String): Response<ApiResponseVoid>
    
    @POST("bookings/validate-session")
    suspend fun validateSession(@Body request: ValidateSessionRequest): Response<ApiResponseValidateSession>
    
    @POST("bookings/validate-promo")
    suspend fun validatePromo(@Body request: ValidatePromoRequest): Response<ApiResponseValidatePromo>

    @GET("tours")
    suspend fun getTours(
        @Query("destination") destination: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("startDate") startDate: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponsePageTourSummaryDto>

    @GET("categories")
    suspend fun getCategories(): Response<ApiResponseListCategory>

    @GET("tours/{id}")
    suspend fun getTourDetail(@Path("id") id: String): Response<ApiResponseTourDetailDto>

    @POST("bookings/{id}/momo-pay-url")
    suspend fun getMomoPaymentUrl(@Path("id") id: String): Response<ApiResponseMomoPayUrlResponse>
}
