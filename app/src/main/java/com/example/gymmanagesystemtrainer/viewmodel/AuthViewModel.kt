package com.example.gymmanagesystemtrainer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gymmanagesystemtrainer.model.AuthResponse
import com.example.gymmanagesystemtrainer.repositories.AuthRepository
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _authState = MutableStateFlow<DataState<AuthResponse>>(DataState.Idle)
    val authState: StateFlow<DataState<AuthResponse>> = _authState
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onUsernameChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = DataState.Loading
            try {
                val response = authRepository.login(email, password)
                if (response.user.role != "Staff") {
                    _authState.value = DataState.Error("Username or password is invalid")
                    return@launch
                }
                sessionManager.saveAuthResponse(response)
                _authState.value = DataState.Success(response)
                _email.value = ""
                _password.value = ""
                _isLoggedIn.value = sessionManager.isLoggedIn()
            } catch (e: HttpException) {
                e.printStackTrace()
                _authState.value = DataState.Error(e.response()?.errorBody()?.string() ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = DataState.Loading
            try {
                sessionManager.clearAccessToken()
                _isLoggedIn.value = sessionManager.isLoggedIn()
                _authState.value = DataState.Idle
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}