package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Feedback
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApiService {
    @POST("slot-feedbacks/filter")
    suspend fun getFeedbacks(@Body filter: FilterRequestBody): Response<Feedback>
}