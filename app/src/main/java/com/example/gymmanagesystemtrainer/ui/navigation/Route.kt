package com.example.gymmanagesystemtrainer.ui.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Dashboard : Route("dashboard")
    data object Profile : Route("profile")
    data object Home : Route("home")
    data object Signup : Route("signup")
    data object ProfileDetail : Route("profileDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "profileDetail/$id"
        }
    }
    data object Class : Route("class")
    data object AllClass : Route("allClass")
    data object ClassDetail : Route("classDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "classDetail/$id"
        }
    }
}