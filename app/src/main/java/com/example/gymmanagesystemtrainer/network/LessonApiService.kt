package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.model.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LessonApiService {
    @POST("slots/filter")
    suspend fun getLessons(@Body filterRequestBody: FilterRequestBody) : Response<Lesson>

    @POST("slots/{id}")
    suspend fun getLessonsByClassId(@Path("id") id: String): Lesson
}