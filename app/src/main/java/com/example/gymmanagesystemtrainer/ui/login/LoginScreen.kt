package com.example.gymmanagesystemtrainer.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gymmanagesystemtrainer.ui.component.LargeButton
import com.example.gymmanagesystemtrainer.ui.component.TextField
import com.example.gymmanagesystemtrainer.ui.navigation.Route
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
) {
    val authState by authViewModel.authState.collectAsState()
    val isLoading = authState is DataState.Loading
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email, label = "Email", onTextChange = authViewModel::onUsernameChange)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, label = "Password", visualTransformation = PasswordVisualTransformation(), onTextChange = authViewModel::onPasswordChange)
        Spacer(modifier = Modifier.height(32.dp))
        LargeButton(text = "Login", isLoading = isLoading, onClick = {authViewModel.login(email, password)}, enabled = email.isNotEmpty() && password.isNotEmpty())
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            navController.navigate(Route.Signup.route)
        }) {
            Text(text = "Don't have an account? Register now!")
        }
        when(authState) {
//            is DataState.Loading -> {
//                CircularProgressIndicator()
//            }
//            is DataState.Success -> {
//                Toast.makeText(null, "Login success", Toast.LENGTH_SHORT).show()
//                onLoginSuccess()
//            }
            is DataState.Error -> {
                val error = (authState as DataState.Error).message
                Text(text = error, modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}