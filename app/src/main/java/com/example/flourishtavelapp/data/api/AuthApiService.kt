package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseAuthResponse
import com.example.flourishtravelapp.data.model.ApiResponseVoid
import com.example.flourishtravelapp.data.model.ChangePasswordRequest
import com.example.flourishtravelapp.data.model.LoginRequest
import com.example.flourishtravelapp.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponseAuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponseAuthResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponseVoid>

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ApiResponseVoid>
}
