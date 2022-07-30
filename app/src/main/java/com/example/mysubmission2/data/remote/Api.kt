package com.example.mysubmission2.data.remote

import com.example.mysubmission2.data.response.AddResponse
import com.example.mysubmission2.data.response.LoginResponse
import com.example.mysubmission2.data.response.RegisterResponse
import com.example.mysubmission2.data.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field ("name") name: String,
        @Field ("email") email: String,
        @Field ("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") auth: String,
        @Query("page")page: Int,
        @Query("size")size: Int
    ): StoriesResponse

    @GET("stories?location=1")
    fun getAllStoryMaps(
        @Header("Authorization") auth: String,
        @Query("location")location: Int
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?
    ): Call<AddResponse>

}