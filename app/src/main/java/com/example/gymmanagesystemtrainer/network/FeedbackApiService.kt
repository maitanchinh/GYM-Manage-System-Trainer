package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Feedback
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FeedbackApiService {
    @POST("slot-feedbacks/filter")
    suspend fun getFeedbacks(@Body filter: FilterRequestBody): Response<Feedback>

    @PUT("slots/{id}/open-close-feedback")
    suspend fun openCloseFeedback(@Path("id") slotId: String): Lesson
}