package com.example.gymmanagesystemtrainer.repositories

import com.example.gymmanagesystemtrainer.model.Attendance
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.AttendanceApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val attendanceApiService: AttendanceApiService){
    suspend fun getAttendances(filterRequestBody: FilterRequestBody) = attendanceApiService.getAttendances(filterRequestBody)

    suspend fun createAttendance(memberId: String, slotId: String) : Attendance {
        val memberIdPart = memberId.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val slotIdPart = slotId.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        return attendanceApiService.createAttendance(memberId = memberIdPart, slotId = slotIdPart)
    }
}