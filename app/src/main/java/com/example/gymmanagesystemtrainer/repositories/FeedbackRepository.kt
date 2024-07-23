package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.FeedbackApiService
import javax.inject.Inject

class FeedbackRepository @Inject constructor(private val feedbackApiService: FeedbackApiService) {
    suspend fun getFeedbacks(filterRequestBody: FilterRequestBody) = feedbackApiService.getFeedbacks(filterRequestBody)

    suspend fun openCloseFeedback(slotId: String) = feedbackApiService.openCloseFeedback(slotId)
}