package com.riyandifirman.storyapp.settings

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            // membuat interceptor
            val authInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            // membuat client baru dengan interceptor yang sudah dibuat
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            // membuat retrofit baru dengan client yang sudah dibuat dan base url dicoding api
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}