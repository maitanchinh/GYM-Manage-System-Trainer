package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.AuthResponse
import com.example.gymmanagesystemtrainer.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse
}