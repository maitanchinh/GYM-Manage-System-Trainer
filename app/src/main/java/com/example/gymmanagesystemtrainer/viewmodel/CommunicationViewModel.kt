package com.example.gymmanagesystemtrainer.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gymmanagesystemtrainer.model.Comment
import com.example.gymmanagesystemtrainer.model.Communication
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Response
import com.example.gymmanagesystemtrainer.repositories.CommunicationRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(private val communicationRepository: CommunicationRepository) :
    ViewModel() {
    private val _communication =
        MutableStateFlow<DataState<Response<Communication>>>(DataState.Idle)
    val communication: StateFlow<DataState<Response<Communication>>> = _communication
    private val _isCommenting = MutableStateFlow(false)
    val isCommenting: StateFlow<Boolean> = _isCommenting

    //    private val _isPosting = MutableStateFlow(false)
//    val isPosting: StateFlow<Boolean> = _isPosting
    private val _comment = MutableStateFlow("")
    val comment: StateFlow<String> = _comment
    private val _communicationContent = MutableStateFlow("")
    val communicationContent: StateFlow<String> = _communicationContent
    private val _communicationImage = MutableStateFlow<File?>(null)
    val communicationImage: StateFlow<File?> = _communicationImage
    private val _selectedCommunication = MutableStateFlow<Communication?>(null)
    val selectedCommunication: StateFlow<Communication?> = _selectedCommunication
    private val _sendComment = MutableStateFlow<DataState<Comment>>(DataState.Idle)
    val sendComment: StateFlow<DataState<Comment>> = _sendComment
    private val _sendCommunication = MutableStateFlow<DataState<Communication>>(DataState.Idle)
    val sendCommunication: StateFlow<DataState<Communication>> = _sendCommunication
    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> = _imageBitmap

    init {
        fetchCommunications(
            FilterRequestBody(
                orderBy = "CreateAt",
                isAscending = false
            )
        )
    }

    fun fetchCommunications(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _communication.value = DataState.Loading
            try {
                val response: Response<Communication> =
                    communicationRepository.getCommunications(filterRequestBody)
                _communication.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at fetchCommunications: ${e.message}")
                _communication.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setIsReplying() {
        _isCommenting.value = !_isCommenting.value
    }

    fun setComment(comment: String) {
        _comment.value = comment
    }

    fun setSelectedCommunication(communication: Communication) {
        _selectedCommunication.value = communication
    }

    fun setCommunicationContent(content: String) {
        _communicationContent.value = content
    }

    fun setCommunicationImage(image: File?) {
        _communicationImage.value = image
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        _imageBitmap.value = bitmap
    }

    fun postCommunication(classId: String) {
        _sendCommunication.value = DataState.Loading
        viewModelScope.launch {
            try {
                val response: Communication = communicationRepository.postCommunication(
                    classId = classId,
                    message = _communicationContent.value,
                    imageFile = _communicationImage.value
                )
                _sendCommunication.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at postCommunication: ${e.message}")
                _sendCommunication.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun sendComment(communicationId: String) {
        _sendComment.value = DataState.Loading
        viewModelScope.launch {
            try {
                val response: Comment = communicationRepository.sendComment(
                    communicationId = communicationId,
                    message = _comment.value
                )
                _sendComment.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at sendComment: ${e.message}")
                _sendComment.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearAllState() {
        _communication.value = DataState.Idle
        _isCommenting.value = false
        _comment.value = ""
        _selectedCommunication.value = null
        _sendComment.value = DataState.Idle
        _sendCommunication.value = DataState.Idle
        _communicationContent.value = ""
        _imageBitmap.value = null
        _communicationImage.value = null
    }
}