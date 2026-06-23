package com.example.flourishtavelapp.data.api

import com.example.flourishtavelapp.BuildConfig
import com.example.flourishtavelapp.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // BASE_URL is read dynamically from the root .env file at compile-time.
    var BASE_URL = BuildConfig.BASE_URL

    private var sessionManager: SessionManager? = null

    fun init(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        val redacted = message
            .replace(Regex("\"latitude\"\\s*:\\s*-?[\\d.]+"), "\"latitude\":\"[redacted]\"")
            .replace(Regex("\"longitude\"\\s*:\\s*-?[\\d.]+"), "\"longitude\":\"[redacted]\"")
            .replace(Regex("\"foodAllergies\"\\s*:\\s*\\[[^\\]]*]"), "\"foodAllergies\":\"[redacted]\"")
            .replace(Regex("\"comment\"\\s*:\\s*\"[^\"]*\""), "\"comment\":\"[redacted]\"")
            .replace(Regex("\"token\"\\s*:\\s*\"[^\"]+\""), "\"token\":\"[redacted]\"")
            .replace(Regex("Bearer\\s+\\S+"), "Bearer [redacted]")
        android.util.Log.d("OkHttp", redacted)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val path = request.url.encodedPath
        val token = sessionManager?.getAccessToken()
        
        val newRequest = if (!token.isNullOrEmpty() && !path.contains("/auth/login") && !path.contains("/auth/register")) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val authApiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    val bookingApiService: BookingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookingApiService::class.java)
    }

    val uploadApiService: UploadApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UploadApiService::class.java)
    }

    val favoriteApiService: FavoriteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FavoriteApiService::class.java)
    }

    val chatbotApiService: ChatbotApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatbotApiService::class.java)
    }

    val floraApiService: FloraApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FloraApiService::class.java)
    }

    val reviewApiService: ReviewApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewApiService::class.java)
    }

    val pushDeviceApiService: PushDeviceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PushDeviceApiService::class.java)
    }
}
