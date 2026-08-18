package com.example.flourishtravelapp.data.api

import com.example.flourishtravelapp.data.model.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface GoidbApiService {

    @POST("/auth/facebook")
    suspend fun post_auth_facebook(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/auth/oauth")
    suspend fun post_auth_oauth(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/auth/refresh")
    suspend fun post_auth_refresh(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/auth/reset-password")
    suspend fun post_auth_reset_password(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/bookings/{id}/request-refund")
    suspend fun post_bookings_id_request_refund(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @DELETE("/tours/{id}")
    suspend fun delete_tours_id(
        @Path("id") id: String,
    ): Response<ApiResponse<JsonObject>>

    @GET("/tours/availability/check")
    suspend fun get_tours_availability_check(
        @Query("destinationSlug") destinationSlug: String? = null,
        @Query("date") date: String? = null,
        @Query("pax") pax: Int? = null,
    ): Response<ApiResponse<JsonObject>>

    @GET("/tours/by-slug/{slug}")
    suspend fun get_tours_by_slug_slug(
        @Path("slug") slug: String,
    ): Response<ApiResponse<JsonObject>>

    @GET("/tours/{id}/similar")
    suspend fun get_tours_id_similar(
        @Path("id") id: String,
    ): Response<ApiResponse<List<JsonObject>>>

    @POST("/tours")
    suspend fun post_tours(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @PUT("/tours/{id}")
    suspend fun put_tours_id(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/catalog")
    suspend fun get_catalog(
    ): Response<ApiResponse<JsonObject>>

    @GET("/catalog/tours/{id}/detail")
    suspend fun get_catalog_tours_id_detail(
        @Path("id") id: String,
    ): Response<ApiResponse<JsonObject>>

    @POST("/catalog/flora-recommend")
    suspend fun post_catalog_flora_recommend(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @DELETE("/categories/{id}")
    suspend fun delete_categories_id(
        @Path("id") id: String,
    ): Response<ApiResponse<JsonObject>>

    @GET("/categories/archived")
    suspend fun get_categories_archived(
    ): Response<ApiResponse<List<JsonObject>>>

    @GET("/categories/{id}")
    suspend fun get_categories_id(
        @Path("id") id: String,
    ): Response<ApiResponse<JsonObject>>

    @POST("/categories")
    suspend fun post_categories(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/categories/{id}/restore")
    suspend fun post_categories_id_restore(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @PUT("/categories/{id}")
    suspend fun put_categories_id(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/destinations/flora-match")
    suspend fun post_destinations_flora_match(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/chat/rooms/{roomId}/messages")
    suspend fun get_chat_rooms_roomId_messages(
        @Path("roomId") roomId: String,
    ): Response<ApiResponse<List<JsonObject>>>

    @PATCH("/chat/messages/{id}/pin")
    suspend fun patch_chat_messages_id_pin(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @PATCH("/chat/messages/{id}/unpin")
    suspend fun patch_chat_messages_id_unpin(
        @Path("id") id: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/chat/messages/{messageId}/reactions")
    suspend fun post_chat_messages_messageId_reactions(
        @Path("messageId") messageId: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/chatbot/config")
    suspend fun get_chatbot_config(
    ): Response<ApiResponse<JsonObject>>

    @GET("/chatbot/config/intents")
    suspend fun get_chatbot_config_intents(
    ): Response<ApiResponse<JsonObject>>

    @POST("/chatbot/config/import")
    suspend fun post_chatbot_config_import(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/flora/bookings/{bookingId}/location")
    suspend fun post_flora_bookings_bookingId_location(
        @Path("bookingId") bookingId: String,

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/guide/sessions/{sessionId}/members")
    suspend fun get_guide_sessions_sessionId_members(
        @Path("sessionId") sessionId: String,
    ): Response<ApiResponse<List<JsonObject>>>

    @GET("/users/me/travel-preferences")
    suspend fun get_users_me_travel_preferences(
    ): Response<ApiResponse<JsonObject>>

    @PATCH("/users/me/travel-preferences")
    suspend fun patch_users_me_travel_preferences(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/content")
    suspend fun get_content(
    ): Response<ApiResponse<List<JsonObject>>>

    @GET("/content/{slug}")
    suspend fun get_content_slug(
        @Path("slug") slug: String,
    ): Response<ApiResponse<JsonObject>>

    @POST("/waitlist")
    suspend fun post_waitlist(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/health")
    suspend fun get_health(
    ): Response<ApiResponse<JsonObject>>

    @POST("/contact-requests")
    suspend fun post_contact_requests(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @POST("/contact-requests/newsletter")
    suspend fun post_contact_requests_newsletter(

    @Body body: Map<String, Any?>,
    ): Response<ApiResponse<JsonObject>>

    @GET("/guides")
    suspend fun get_guides(
    ): Response<ApiResponse<List<JsonObject>>>

    @GET("/guides/{id}")
    suspend fun get_guides_id(
        @Path("id") id: String,
    ): Response<ApiResponse<JsonObject>>
}
