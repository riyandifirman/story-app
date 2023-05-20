package com.riyandifirman.storyapp.settings

import com.riyandifirman.storyapp.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //fungsi untuk mengirimkan data saat register
    @POST("v1/register")
    @FormUrlEncoded
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    // fungsi untuk mengirimkan data saat login
    @POST("v1/login")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // fungsi untuk mendapatkan semua data story
    @GET("v1/stories")
    fun getAllStory(
        @Header("Authorization") Bearer: String
    ): Call<GetStoryResponse>

    // fungsi untuk mengirimkan data saat upload story
    @Multipart
    @POST("v1/stories")
    fun uploadStory(
        @Header("Authorization") Bearer: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

    // fungsi untuk mendapatkan detail story
    @GET("v1/stories/{id}")
    fun getDetailStory(
        @Header("Authorization") Bearer: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    // fungsi untuk mengambil lokasi story
    @GET("v1/stories")
    fun getStoryLocation(
        @Header("Authorization") Bearer: String,
        @Query("location") location: Int = 0
    ): Call<GetStoryResponse>
}