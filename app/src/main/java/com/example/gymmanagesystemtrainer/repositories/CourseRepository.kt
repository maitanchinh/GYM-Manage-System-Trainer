package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.CourseApiService
import javax.inject.Inject

class CourseRepository @Inject constructor(private val courseApiService: CourseApiService) {
    suspend fun getCourses(filterRequestBody: FilterRequestBody) = courseApiService.getCourses(filterRequestBody)

    suspend fun getCourseById(id: String) = courseApiService.getCourseById(id)
}