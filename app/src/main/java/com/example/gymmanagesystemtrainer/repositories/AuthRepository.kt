package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.AuthResponse
import com.example.gymmanagesystemtrainer.model.LoginRequest
import com.example.gymmanagesystemtrainer.network.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun login(email: String, password: String): AuthResponse {
        val loginRequest = LoginRequest(email, password)
        return authApiService.login(loginRequest)
    }
}