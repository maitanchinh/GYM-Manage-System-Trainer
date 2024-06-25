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
import com.example.gymmanagesystemtrainer.ui.gymclass.detail.ClassDetailScreen
import com.example.gymmanagesystemtrainer.ui.home.HomeScreen
import com.example.gymmanagesystemtrainer.ui.login.LoginScreen
import com.example.gymmanagesystemtrainer.ui.profile.ProfileDetailScreen
import com.example.gymmanagesystemtrainer.ui.profile.ProfileScreen
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
        startDestination = Route.Home.route,
        modifier = modifier,
    ) {
        composable(Route.Home.route) { HomeScreen() }
        composable(Route.Profile.route) {
            ProfileScreen(onProfileDetailClick = { id ->
                navController.navigate(Route.ProfileDetail.createRouteWithId(id))
            },
                onLogoutClick = onLogoutClick)
        }
        composable(Route.Class.route) {
            ClassScreen(
                onViewAllMyClassClick = { navController.navigate(Route.AllClass.route) },
                onClassClick = { id -> navController.navigate(Route.ClassDetail.createRouteWithId(id)) })
        }
        composable(Route.Signup.route) {
            SignupScreen()
        }
        composable(Route.ProfileDetail.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            ProfileDetailScreen(userId = userId!!)
        }
        composable(Route.AllClass.route) {
            AllClassScreen(onClassClick = { id ->
                navController.navigate(
                    Route.ClassDetail.createRouteWithId(
                        id
                    )
                )
            })
        }
        composable(Route.ClassDetail.route) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("id")
            ClassDetailScreen(classId = classId!!)
        }
    }
}