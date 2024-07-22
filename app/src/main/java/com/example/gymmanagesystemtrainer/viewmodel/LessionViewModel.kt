package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.repositories.LessonRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(private val lessonRepository: LessonRepository) : ViewModel() {
    private val _lessons = MutableStateFlow<DataState<Response<Lesson>>>(DataState.Idle)
    val lessons : StateFlow<DataState<Response<Lesson>>> = _lessons

    fun getLessons(filterRequestBody: FilterRequestBody) {
       viewModelScope.launch {
            _lessons.value = DataState.Loading
            try {
                val response: Response<Lesson> = lessonRepository.getLessons(filterRequestBody)
                _lessons.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at getLessons: ${e.message}")
                _lessons.value = DataState.Error(e.message ?: "Unknown error")
            }
       }
    }
}