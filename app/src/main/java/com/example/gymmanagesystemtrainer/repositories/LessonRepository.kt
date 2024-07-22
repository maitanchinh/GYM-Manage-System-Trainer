package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.LessonApiService
import javax.inject.Inject

class LessonRepository @Inject constructor(private val lessonApiService: LessonApiService) {
    suspend fun getLessons(filterRequestBody: FilterRequestBody) = lessonApiService.getLessons(filterRequestBody)
}