package com.example.gymmanagesystemtrainer.ui.navigation

import com.example.gymmanagesystemtrainer.R

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    data object Schedule : BottomNavItem("Schedule", R.drawable.round_calendar_month_24, Route.Schedule.route)
    data object Profile : BottomNavItem("Profile", R.drawable.round_person_24, Route.Profile.route)
//    data object Class : BottomNavItem("Class", R.drawable.round_class_24, Route.Class.route)
}