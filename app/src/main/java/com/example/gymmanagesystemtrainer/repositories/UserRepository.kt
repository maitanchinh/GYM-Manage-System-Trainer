package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.network.UserApiService
import javax.inject.Inject


class UserRepository @Inject constructor(private val userApiService: UserApiService) {
    suspend fun getUserById(id: String) = userApiService.getUserById(id)
}