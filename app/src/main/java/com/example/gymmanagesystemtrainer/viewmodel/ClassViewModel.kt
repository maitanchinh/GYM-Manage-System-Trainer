package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gymmanagesystemtrainer.model.Classes
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.GClass
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.repositories.ClassRepository
import com.example.gymmanagesystemtrainer.repositories.LessonRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassViewModel @Inject constructor(private val classRepository: ClassRepository, private val lessonRepository: LessonRepository,) :
    ViewModel() {

//    private val _classes = MutableStateFlow(Classes())
    private val _classes = MutableStateFlow<DataState<Response<GClass>>>(DataState.Loading)
    private val _class = MutableStateFlow(GClass())
//    val classes: StateFlow<Classes> = _classes
    val classes: StateFlow<DataState<Response<GClass>>> = _classes
    val gClass: StateFlow<GClass> = _class
    private val _lessons = MutableStateFlow(Response<Lesson>())
    val lessons: StateFlow<Response<Lesson>> = _lessons
    private val _classInDate = MutableStateFlow<DataState<Response<GClass>>>(DataState.Idle)
    val classInDate: StateFlow<DataState<Response<GClass>>> = _classInDate
    init {
        fetchClasses()
    }

    fun refreshClasses() {
        fetchClasses()
    }

    private fun fetchClasses() {
        viewModelScope.launch {
            try {
                val response = classRepository.getClasses()
                println("API Response: $response")
                _classes.value = DataState.Success(response)
//                _classes.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error: ${e.message}")
                _classes.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchLessons(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            try {
                val response = lessonRepository.getLessons(filterRequestBody)
                _lessons.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchLessons: ${e.message}")
            }
        }
    }

    fun getClassById(id: String) {
        viewModelScope.launch {
            try {
                val response = classRepository.getClassById(id)
                _class.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchClassesEnrolled(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _classes.value = DataState.Loading
            try {
                val response: Response<GClass> = classRepository.getClassesEnrolled(filterRequestBody)
                _classes.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchClassesEnrolled: ${e.message}")
                _classes.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchClassesInDate(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _classInDate.value = DataState.Loading
            try {
                val response: Response<GClass> = classRepository.getClassesEnrolled(filterRequestBody)
                _classInDate.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchClassesInDate: ${e.message}")
                _classInDate.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}