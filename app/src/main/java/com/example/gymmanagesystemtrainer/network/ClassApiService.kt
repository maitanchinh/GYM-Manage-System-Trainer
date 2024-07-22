package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Classes
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.GClass
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClassApiService{
    @GET("classes")
    suspend fun getClasses(): Response<GClass>

    @GET("classes/{id}")
    suspend fun getClassById(@Path("id") id: String): GClass

    @RequiresAuth
    @POST("classes/trainers")
    suspend fun getClassesEnrolled(@Body filterRequestBody: FilterRequestBody): Response<GClass>
}