package com.example.shalenamma.ui.navigation

sealed class Screen(val route: String) {
    object RoleSelection : Screen("role_selection")
    object Home : Screen("home")
    object Announcements : Screen("announcements")
    object Calendar : Screen("calendar")
    object Profile : Screen("profile")
    object DailyMeal : Screen("daily_meal")
    object FacilityTour : Screen("facility_tour")
    object StudentStars : Screen("student_stars")
    object Feedback : Screen("feedback")
}
