package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {
    @GET("trainers/{id}")
    suspend fun getUserById(@Path("id") id: String?) : User

    @Multipart
    @POST("trainers")
    suspend fun signUp(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody
    ): User

    @Multipart
    @PUT("trainers/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Part("name") name: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("dateOfBirth") dateOfBirth: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part avatar: MultipartBody.Part?
    ): User
}