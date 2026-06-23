package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.data.model.ApiEnvelope
import com.example.flourishtavelapp.data.model.PushDeviceRegisterRequest
import com.example.flourishtavelapp.data.model.PushDeviceRegisterResponse
import com.example.flourishtavelapp.data.model.PushDeviceStatusResponse
import com.example.flourishtavelapp.data.model.PushDeviceUnregisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PushDeviceApiService {

    @POST("notifications/push-devices")
    suspend fun register(@Body body: PushDeviceRegisterRequest): Response<ApiEnvelope<PushDeviceRegisterResponse>>

    @POST("notifications/push-devices/unregister")
    suspend fun unregister(@Body body: PushDeviceUnregisterRequest): Response<ApiEnvelope<Unit>>

    @GET("notifications/push-devices/status")
    suspend fun status(): Response<ApiEnvelope<PushDeviceStatusResponse>>
}
