package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Classes
import com.example.gymmanagesystemtrainer.model.GClass
import retrofit2.http.GET
import retrofit2.http.Path

interface ClassApiService{
    @GET("classes")
    suspend fun getClasses(): Classes

    @GET("classes/{id}")
    suspend fun getClassById(@Path("id") id: String): GClass
}