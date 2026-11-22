package com.devfest.wellnest.ui.dashboard

import com.devfest.wellnest.data.calendar.CalendarEvent
import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.local.entity.ScheduleEntity

data class DashboardUiState(
    val isLoading: Boolean = false,
    val greeting: String = "Good morning",
    val activeSchedule: ScheduleEntity? = null,
    val todayHabits: List<HabitEntity> = emptyList(),
    val calendarEvents: List<CalendarEvent> = emptyList(),
    val completedHabitsCount: Int = 0,
    val error: String? = null
)
