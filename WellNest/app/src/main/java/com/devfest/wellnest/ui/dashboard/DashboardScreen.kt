package com.devfest.wellnest.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devfest.wellnest.data.calendar.CalendarEvent
import com.devfest.wellnest.data.local.entity.HabitEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.greeting,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "WellNest",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Today's date card
                item {
                    TodayDateCard()
                }

                // Schedule summary
                uiState.activeSchedule?.let { schedule ->
                    item {
                        ScheduleSummaryCard(
                            startHour = schedule.workStartHour,
                            startMin = schedule.workStartMinute,
                            endHour = schedule.workEndHour,
                            endMin = schedule.workEndMinute
                        )
                    }
                }

                // Habits progress
                if (uiState.todayHabits.isNotEmpty()) {
                    item {
                        Text(
                            text = "Today's Habits",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    items(uiState.todayHabits.take(3)) { habit ->
                        HabitQuickCard(habit = habit)
                    }
                }

                // Calendar events
                if (uiState.calendarEvents.isNotEmpty()) {
                    item {
                        Text(
                            text = "Upcoming Events",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    items(uiState.calendarEvents.take(3)) { event ->
                        CalendarEventCard(event = event)
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayDateCard() {
    val today = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = today, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ScheduleSummaryCard(startHour: Int, startMin: Int, endHour: Int, endMin: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Column {
                Text("Work Schedule", style = MaterialTheme.typography.labelMedium)
                Text(
                    "%02d:%02d – %02d:%02d".format(startHour, startMin, endHour, endMin),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HabitQuickCard(habit: HabitEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.name, style = MaterialTheme.typography.bodyLarge)
                Text(habit.category, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Icon(Icons.Filled.CheckCircle, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun CalendarEventCard(event: CalendarEvent) {
    val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(event.startTimeMs))
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(startTime, style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Column {
                Text(event.title, style = MaterialTheme.typography.bodyLarge)
                if (event.description.isNotBlank()) {
                    Text(event.description, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }
}
