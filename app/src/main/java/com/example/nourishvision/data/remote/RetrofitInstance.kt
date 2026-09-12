package com.example.nourishvision.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://nourishvision-backend-production.up.railway.app/"
    private const val PREDICT_BASE_URL = "https://web-production-a182e.up.railway.app/"

    private val logging = HttpLoggingInterceptor { message ->
        if (!message.contains("\ufffd") && message.length < 1000) {
            android.util.Log.d("OkHttp", message)
        }
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    val predictApi: PredictApiService = Retrofit.Builder()
        .baseUrl(PREDICT_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PredictApiService::class.java)
}