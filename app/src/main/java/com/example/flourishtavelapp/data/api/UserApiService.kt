package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseUserProfile
import com.example.flourishtravelapp.data.model.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {
    @GET("users/me")
    suspend fun getProfile(): Response<ApiResponseUserProfile>
    
    @PATCH("users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ApiResponseUserProfile>
}
