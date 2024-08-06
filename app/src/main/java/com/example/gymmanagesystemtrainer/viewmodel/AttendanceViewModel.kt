package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gymmanagesystemtrainer.model.Attendance
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.repositories.AttendanceRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {
    private val _attendances = MutableStateFlow<DataState<Response<Attendance>>>(DataState.Idle)
    val attendances: StateFlow<DataState<Response<Attendance>>> = _attendances
    private val _attendance = MutableStateFlow<DataState<Attendance>>(DataState.Idle)
    val attendance: StateFlow<DataState<Attendance>> = _attendance
    private val _trainerAttendances = MutableStateFlow<DataState<Response<Attendance>>>(DataState.Idle)
    val trainerAttendances: StateFlow<DataState<Response<Attendance>>> = _trainerAttendances

    fun fetchAttendances(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _attendances.value = DataState.Loading
            try {
                val response: Response<Attendance> =
                    attendanceRepository.getAttendances(filterRequestBody)
                _attendances.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchAttendances: ${e.message}")
                _attendances.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createAttendance(memberId: String, slotId: String) {
        viewModelScope.launch {
            _attendance.value = DataState.Loading
            try {
                val attendance: Attendance = attendanceRepository.createAttendance(memberId = memberId, slotId = slotId)
                _attendance.value = DataState.Success(attendance)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at createAttendance: ${e.message}")
                _attendance.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchTrainerAttendances(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _trainerAttendances.value = DataState.Loading
            try {
                val response: Response<Attendance> =
                    attendanceRepository.getTrainerAttendances(filterRequestBody)
                _trainerAttendances.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchTrainerAttendances: ${e.message}")
                _trainerAttendances.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}