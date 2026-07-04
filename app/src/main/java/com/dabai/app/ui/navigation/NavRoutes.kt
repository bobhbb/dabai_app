package com.dabai.app.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Splash : NavRoutes("splash")
    data object Login : NavRoutes("login")
    data object Home : NavRoutes("home")
    data object Camera : NavRoutes("camera/{mode}") {
        fun createRoute(mode: String = "normal") = "camera/$mode"
    }
    data object PersonDetail : NavRoutes("person/{personId}") {
        fun createRoute(personId: String) = "person/$personId"
    }
    data object HealthReport : NavRoutes("report/{personId}/{recordId}") {
        fun createRoute(personId: String, recordId: String) = "report/$personId/$recordId"
    }
    data object TodoList : NavRoutes("todo/{personId}") {
        fun createRoute(personId: String) = "todo/$personId"
    }
    data object NewPerson : NavRoutes("new-person/{faceToken}/{photoPath}") {
        fun createRoute(faceToken: String, photoPath: String) = "new-person/$faceToken/$photoPath"
    }
}
