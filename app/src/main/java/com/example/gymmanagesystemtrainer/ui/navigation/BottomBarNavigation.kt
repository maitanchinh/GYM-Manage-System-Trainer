package com.example.gymmanagesystemtrainer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymmanagesystemtrainer.ui.gymclass.AllClassScreen
import com.example.gymmanagesystemtrainer.ui.gymclass.ClassScreen
import com.example.gymmanagesystemtrainer.ui.gymclass.component.QRScanner
import com.example.gymmanagesystemtrainer.ui.gymclass.detail.ClassDetailScreen
import com.example.gymmanagesystemtrainer.ui.home.HomeScreen
import com.example.gymmanagesystemtrainer.ui.login.LoginScreen
import com.example.gymmanagesystemtrainer.ui.profile.ProfileDetailScreen
import com.example.gymmanagesystemtrainer.ui.profile.ProfileScreen
import com.example.gymmanagesystemtrainer.ui.schedule.ScheduleScreen
import com.example.gymmanagesystemtrainer.ui.signup.SignupScreen
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.viewmodel.AuthViewModel

const val BOTTOM_BAR_ROUTE = "bottomBar"

@Composable
fun BottomBarNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Schedule.route,
        modifier = modifier,
    ) {
        composable(Route.Schedule.route) { ScheduleScreen(onClassClick = { courseId, classId ->
            navController.navigate(Route.ClassDetail.createRouteWithId(courseId, classId))
        }) }
        composable(Route.Profile.route) {
            ProfileScreen(onProfileDetailClick = { id ->
                navController.navigate(Route.ProfileDetail.createRouteWithId(id))
            },
                onLogoutClick = onLogoutClick)
        }
        composable(Route.ClassDetail.route) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val classId = backStackEntry.arguments?.getString("classId")
            ClassDetailScreen(courseId = courseId!!, classId = classId!!, onScanClick = {
                navController.navigate(Route.QRScanner.route)
            })
        }
        composable(Route.Signup.route) {
            SignupScreen()
        }
        composable(Route.ProfileDetail.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            ProfileDetailScreen(userId = userId!!)
        }
        composable(Route.QRScanner.route) {
            QRScanner()
        }
    }
}