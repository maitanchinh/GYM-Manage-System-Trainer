package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET("members/{id}")
    suspend fun getUserById(@Path("id") id: String?) : User
}