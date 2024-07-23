package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagesystemtrainer.model.Feedback
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.repositories.FeedbackRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(private val feedbackRepository: FeedbackRepository) : ViewModel() {
    private val _feedbacks = MutableStateFlow<DataState<Response<Feedback>>>(DataState.Idle)
    val feedbacks : MutableStateFlow<DataState<Response<Feedback>>> = _feedbacks
    private val _isShowOpenFeedbackDialog = MutableStateFlow(false)
    val isShowOpenFeedbackDialog: MutableStateFlow<Boolean> = _isShowOpenFeedbackDialog
    private val _lessonChangedFeedbackStatus = MutableStateFlow<Lesson?>(null)
    val lessonChangedFeedbackStatus: MutableStateFlow<Lesson?> = _lessonChangedFeedbackStatus
    private val _openCloseFeedback = MutableStateFlow<DataState<Lesson>>(DataState.Idle)
    val openCloseFeedback: MutableStateFlow<DataState<Lesson>> = _openCloseFeedback

    fun getFeedbacks(filterRequestBody: FilterRequestBody) {
       viewModelScope.launch {
          _feedbacks.value = DataState.Loading
           try {
               val response = feedbackRepository.getFeedbacks(filterRequestBody)
                _feedbacks.value = DataState.Success(response)
           } catch (e: Exception) {
               println("Error at getFeedbacksByClassId: ${e.message}")
                _feedbacks.value = DataState.Error(e.message ?: "Unknown error")
           }
       }
    }

    fun setShowOpenFeedbackDialog() {
        _isShowOpenFeedbackDialog.value = !_isShowOpenFeedbackDialog.value
    }

    fun setLessonChangedFeedbackStatus(lesson: Lesson) {
        _lessonChangedFeedbackStatus.value = lesson
    }

    fun openCloseFeedback(slotId: String) {
        viewModelScope.launch {
            _openCloseFeedback.value = DataState.Loading
            try {
                val lesson = feedbackRepository.openCloseFeedback(slotId)
                _openCloseFeedback.value = DataState.Success(lesson)
            } catch (e: Exception) {
                println("Error at openCloseFeedback: ${e.message}")
                _openCloseFeedback.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}