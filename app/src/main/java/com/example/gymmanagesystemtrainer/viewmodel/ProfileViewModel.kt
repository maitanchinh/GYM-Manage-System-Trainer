package com.example.gymmanagesystemtrainer.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.gymmanagesystemtrainer.model.User
import com.example.gymmanagesystemtrainer.repositories.UserRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.SessionManager
import com.example.gymmanagesystemtrainer.utils.SettingDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userState = MutableStateFlow<DataState<User>>(DataState.Idle)
    val userState : StateFlow<DataState<User>> = _userState
    private val _userUpdated = MutableStateFlow<DataState<User>>(DataState.Idle)
    val userUpdated: StateFlow<DataState<User>> = _userUpdated
    val darkMode = SettingDataStore.getDarkMode(context).asLiveData()
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone
    private val _gender = MutableStateFlow("Choose gender")
    val gender: StateFlow<String> = _gender
    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status
    private val _dateOfBirth = MutableStateFlow("")
    val dateOfBirth: StateFlow<String> = _dateOfBirth
    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> = _imageBitmap
    private val _avatar = MutableStateFlow<File?>(null)
    val avatar: StateFlow<File?> = _avatar

    fun onNameChange(newName: String) {
        _name.value = newName
    }
    fun onPhoneChange(newPhone: String) {
        _phone.value = newPhone
    }

    fun onGenderChange(newGender: String) {
        _gender.value = newGender
    }

    fun onStatusChange(newStatus: String) {
        _status.value = newStatus
    }

    fun onDateOfBirthChange(newDateOfBirth: String) {
        _dateOfBirth.value = newDateOfBirth
    }

    fun setAvatarImage(image: File?) {
        _avatar.value = image
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        _imageBitmap.value = bitmap
    }

    fun onDarkModeSwitchChanged(isDarkMode: Boolean) {
        viewModelScope.launch { SettingDataStore.saveDarkMode(context, isDarkMode) }
    }

    fun getUser(): User? {
        return sessionManager.getUser()
    }

    fun getUserById(id: String) {
        viewModelScope.launch {
            _userState.value = DataState.Loading
            try {
                val response = userRepository.getUserById(id)
                _userState.value = DataState.Success(response)
                _name.value = response.name ?: ""
                _phone.value = response.phone ?: ""
                _gender.value = response.gender ?: ""
                _status.value = response.status ?: ""
                _dateOfBirth.value = response.dateOfBirth ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at getUserById: ${e.message}")
                _userState.value = DataState.Error(e.message ?: "Unknown error")
            }

        }
    }

    fun updateProfile(
        id: String,
        name: String?,
        phone: String?,
        gender: String?,
        status: String?,
        dateOfBirth: String?,
        avatar: File?
    ) {
        viewModelScope.launch {
            _userUpdated.value = DataState.Loading
            try {
                val response = userRepository.updateUser(
                    id = id,
                    name = name,
                    phone = phone,
                    gender = gender,
                    status = status,
                    dateOfBirth = dateOfBirth,
                    avatar = avatar
                )
                _userUpdated.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _userUpdated.value = DataState.Error(if (e.code() == 409) "Phone number already used" else "Unknown error")
            }
        }
    }
}

