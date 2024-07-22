package com.example.gymmanagesystemtrainer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gymmanagesystemtrainer.model.User
import com.example.gymmanagesystemtrainer.repositories.UserRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.SessionManager
import com.example.gymmanagesystemtrainer.utils.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userState = MutableStateFlow<DataState<User>>(DataState.Idle)
    val userState : StateFlow<DataState<User>> = _userState
    val darkMode = SettingDataStore.getDarkMode(context).asLiveData()
    private val _name = MutableStateFlow("")
    val name : StateFlow<String> = _name
    private val _email = MutableStateFlow("")
    val email : StateFlow<String> = _email
    private val _password = MutableStateFlow("")
    val password : StateFlow<String> = _password
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword : StateFlow<String> = _confirmPassword

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
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
            } catch (e: Exception) {
                e.printStackTrace()
                _userState.value = DataState.Error(e.message ?: "Unknown error")
            }

        }
    }

    fun signup(email: String, password: String, name: String) {
        viewModelScope.launch {
            _userState.value = DataState.Loading
            try {
                val response = userRepository.signUp(email = email, password = password, name = name)
                _userState.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _userState.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

