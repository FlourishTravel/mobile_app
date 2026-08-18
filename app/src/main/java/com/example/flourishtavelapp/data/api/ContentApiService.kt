package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseListSiteContent
import com.example.flourishtravelapp.data.model.ApiResponseVoid
import com.example.flourishtravelapp.data.model.ContactRequestCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContentApiService {
    @GET("content")
    suspend fun listContent(@Query("type") type: String? = null): Response<ApiResponseListSiteContent>

    @POST("contact-requests")
    suspend fun createContactRequest(@Body body: ContactRequestCreate): Response<ApiResponseVoid>
}
