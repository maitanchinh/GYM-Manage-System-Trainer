package com.example.gymmanagesystemtrainer.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagesystemtrainer.ui.navigation.BottomBarNavigation
import com.example.gymmanagesystemtrainer.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController, onLogoutClick: () -> Unit){
    val navControllerBottomBar = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController = navControllerBottomBar) }) {
        BottomBarNavigation(navController = navControllerBottomBar, modifier = Modifier.padding(it), onLogoutClick = onLogoutClick)
    }
}
