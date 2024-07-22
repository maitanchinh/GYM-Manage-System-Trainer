package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.CourseCategory
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CategoryApiService {
    @POST("categories/filter")
    suspend fun getCategories(@Body filterRequestBody: FilterRequestBody): Response<CourseCategory>

}