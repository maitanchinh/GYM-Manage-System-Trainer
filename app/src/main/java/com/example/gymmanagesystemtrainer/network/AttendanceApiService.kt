package com.example.gymmanagesystemtrainer.network

import com.example.gymmanagesystemtrainer.model.Attendance
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.utils.RequiresAuth
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AttendanceApiService {
    @POST("attendances/filter")
    suspend fun getAttendances(@Body filterRequestBody: FilterRequestBody): Response<Attendance>

    @RequiresAuth
    @Multipart
    @POST("attendances")
    suspend fun createAttendance(@Part("memberId") memberId: RequestBody, @Part("slotId") slotId: RequestBody): Attendance
}