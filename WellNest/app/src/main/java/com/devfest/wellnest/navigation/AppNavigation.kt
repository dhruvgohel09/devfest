package com.devfest.wellnest.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devfest.wellnest.ui.dashboard.DashboardScreen
import com.devfest.wellnest.ui.goals.GoalsScreen
import com.devfest.wellnest.ui.notes.NotesScreen
import com.devfest.wellnest.ui.schedule.ScheduleScreen
import com.devfest.wellnest.ui.stretch.StretchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Dashboard,
        Screen.Schedule,
        Screen.Goals,
        Screen.Stretch,
        Screen.Notes
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                when (screen) {
                                    Screen.Dashboard -> Icons.Filled.Home
                                    Screen.Schedule -> Icons.Filled.CalendarMonth
                                    Screen.Goals -> Icons.Filled.FitnessCenter
                                    Screen.Stretch -> Icons.Filled.SelfImprovement
                                    Screen.Notes -> Icons.Filled.Note
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Schedule.route) { ScheduleScreen() }
            composable(Screen.Goals.route) { GoalsScreen() }
            composable(Screen.Stretch.route) { StretchScreen() }
            composable(Screen.Notes.route) { NotesScreen() }
        }
    }
}
