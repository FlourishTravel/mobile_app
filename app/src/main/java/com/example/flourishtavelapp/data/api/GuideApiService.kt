package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.ApiResponseGuideExpense
import com.example.flourishtravelapp.data.model.ApiResponseGuideExpenseList
import com.example.flourishtravelapp.data.model.ApiResponseGuideSessionDetail
import com.example.flourishtravelapp.data.model.ApiResponseGuideSessionGuests
import com.example.flourishtravelapp.data.model.ApiResponseGuideSessionList
import com.example.flourishtravelapp.data.model.ApiResponseParticipantActivity
import com.example.flourishtravelapp.data.model.ApiResponseSessionCheckin
import com.example.flourishtravelapp.data.model.ApiResponseSessionParticipant
import com.example.flourishtravelapp.data.model.ApiResponseSessionSchedule
import com.example.flourishtravelapp.data.model.CreateGuideSessionExpenseRequest
import com.example.flourishtravelapp.data.model.GuideCheckinRequest
import com.example.flourishtravelapp.data.model.SessionActivitySchedulePatchRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideApiService {
    @GET("guide/sessions")
    suspend fun getSessions(
        @Query("month") month: Int? = null,
        @Query("year") year: Int? = null
    ): Response<ApiResponseGuideSessionList>

    @GET("guide/sessions/{sessionId}")
    suspend fun getSession(
        @Path("sessionId") sessionId: String
    ): Response<ApiResponseGuideSessionDetail>

    @GET("guide/sessions/{sessionId}/guests")
    suspend fun getSessionGuests(
        @Path("sessionId") sessionId: String
    ): Response<ApiResponseGuideSessionGuests>

    @POST("guide/checkins")
    suspend fun checkin(
        @Body body: GuideCheckinRequest
    ): Response<ApiResponseSessionCheckin>

    @POST("guide/sessions/{sessionId}/participants/{participantId}/check-in")
    suspend fun participantCheckIn(
        @Path("sessionId") sessionId: String,
        @Path("participantId") participantId: String
    ): Response<ApiResponseSessionParticipant>

    @POST("guide/sessions/{sessionId}/participants/{participantId}/check-out")
    suspend fun participantCheckOut(
        @Path("sessionId") sessionId: String,
        @Path("participantId") participantId: String
    ): Response<ApiResponseSessionParticipant>

    @POST("guide/sessions/{sessionId}/participants/{participantId}/activities/{activityId}/check-in")
    suspend fun participantActivityCheckIn(
        @Path("sessionId") sessionId: String,
        @Path("participantId") participantId: String,
        @Path("activityId") activityId: String
    ): Response<ApiResponseParticipantActivity>

    @POST("guide/sessions/{sessionId}/participants/{participantId}/activities/{activityId}/check-out")
    suspend fun participantActivityCheckOut(
        @Path("sessionId") sessionId: String,
        @Path("participantId") participantId: String,
        @Path("activityId") activityId: String
    ): Response<ApiResponseParticipantActivity>

    @GET("guide/sessions/{sessionId}/schedule")
    suspend fun getSessionSchedule(
        @Path("sessionId") sessionId: String
    ): Response<ApiResponseSessionSchedule>

    @PATCH("guide/sessions/{sessionId}/schedule/activities/{activityId}")
    suspend fun patchSessionActivitySchedule(
        @Path("sessionId") sessionId: String,
        @Path("activityId") activityId: String,
        @Body body: SessionActivitySchedulePatchRequest
    ): Response<ApiResponseSessionSchedule>

    @POST("guide/sessions/{sessionId}/schedule/activities/{activityId}/publish")
    suspend fun publishSessionActivitySchedule(
        @Path("sessionId") sessionId: String,
        @Path("activityId") activityId: String
    ): Response<ApiResponseSessionSchedule>

    @POST("guide/sessions/{sessionId}/schedule/activities/{activityId}/cancel")
    suspend fun cancelSessionActivitySchedule(
        @Path("sessionId") sessionId: String,
        @Path("activityId") activityId: String
    ): Response<ApiResponseSessionSchedule>

    @GET("guide/sessions/{sessionId}/expenses")
    suspend fun listSessionExpenses(
        @Path("sessionId") sessionId: String
    ): Response<ApiResponseGuideExpenseList>

    @POST("guide/sessions/{sessionId}/expenses")
    suspend fun createSessionExpense(
        @Path("sessionId") sessionId: String,
        @Body body: CreateGuideSessionExpenseRequest
    ): Response<ApiResponseGuideExpense>

    @DELETE("guide/sessions/{sessionId}/expenses/{expenseId}")
    suspend fun deleteSessionExpense(
        @Path("sessionId") sessionId: String,
        @Path("expenseId") expenseId: String
    ): Response<ApiResponseGuideExpense>
}
