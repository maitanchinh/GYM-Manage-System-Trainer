package com.example.gymmanagesystemtrainer.ui.navigation

import com.example.gymmanagesystemtrainer.R

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    data object Home : BottomNavItem("Home", R.drawable.round_home_24, Route.Home.route)
    data object Profile : BottomNavItem("Profile", R.drawable.round_person_24, Route.Profile.route)
    data object Class : BottomNavItem("Class", R.drawable.round_class_24, Route.Class.route)
}