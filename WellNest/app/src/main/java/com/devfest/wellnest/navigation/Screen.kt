package com.devfest.wellnest.navigation

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Home")
    object Schedule : Screen("schedule", "Schedule")
    object Goals : Screen("goals", "Goals")
    object Stretch : Screen("stretch", "Stretch")
    object Notes : Screen("notes", "Notes")
}
