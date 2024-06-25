package com.example.gymmanagesystemtrainer.model

data class AuthResponse(
    val accessToken: String,
    val user: User
)

data class LoginRequest(
    val email: String,
    val password: String
)