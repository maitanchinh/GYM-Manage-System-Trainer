package com.example.gymmanagesystemtrainer.ui.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Dashboard : Route("dashboard")
    data object Profile : Route("profile")
    data object Schedule : Route("schedule")
    data object Signup : Route("signup")
    data object ProfileDetail : Route("profileDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "profileDetail/$id"
        }
    }
    data object Class : Route("class")
    data object AllClass : Route("allClass")
    data object ClassDetail : Route("schedule/classDetail/{courseId}/{classId}"){
        fun createRouteWithId(courseId: String, classId: String): String {
            return "schedule/classDetail/$courseId/$classId"
        }
    }
    data object QRScanner : Route("schedule/classDetail/qrScanner")
    data object BorrowEquipment : Route("schedule/borrowEquipment/{slotId}"){
        fun createRouteWithId(slotId: String): String {
            return "schedule/borrowEquipment/$slotId"
        }
    }
    data object BorrowRequest : Route("schedule/borrowRequest/{slotId}"){
        fun createRouteWithId(slotId: String): String {
            return "schedule/borrowRequest/$slotId"
        }
    }
}