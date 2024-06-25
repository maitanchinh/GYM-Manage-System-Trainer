package com.example.gymmanagesystemtrainer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymmanagesystemtrainer.ui.dashboard.DashboardScreen
import com.example.gymmanagesystemtrainer.ui.login.LoginScreen
import com.example.gymmanagesystemtrainer.ui.signup.SignupScreen
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.viewmodel.AuthViewModel

const val ROOT_ROUTE = "root"

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        route = ROOT_ROUTE
    ) {
        composable(Route.Login.route) {
            if (!isLoggedIn) {
                LoginScreen(
//                    onLoginSuccess = { navController.navigate(Route.Dashboard.route) },
                    onSignupClick = { navController.navigate(Route.Signup.route) },
                    isLoading = authState is DataState.Loading,
                    onLoginClick = {authViewModel.login(email, password)},
                    authViewModel = authViewModel
                )
            } else {
                DashboardScreen(navController = navController, onLogoutClick = {
                    authViewModel.logout()
                })
            }
        }

        composable(Route.Signup.route) {
            SignupScreen()
        }
    }
}