package com.example.flourishtravelapp.data.model

import com.google.gson.annotations.SerializedName

// GET /chatbot/weather-forecast?destination=...
data class WeatherForecast(
    @SerializedName("destination") val destination: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("days") val days: List<WeatherDay>?
)

data class WeatherDay(
    @SerializedName("date") val date: String?,
    @SerializedName("condition") val condition: String?,
    @SerializedName("tempMin") val tempMin: String?,
    @SerializedName("tempMax") val tempMax: String?
)

data class ApiResponseWeatherForecast(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: WeatherForecast?
)

// GET /chatbot/nearby-places?destination=...&poi_type=...
data class NearbyPlace(
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("distance") val distance: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("address") val address: String?
)

data class ApiResponseNearbyPlace(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: NearbyPlace?
)
