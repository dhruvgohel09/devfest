package com.devfest.wellnest.ui.schedule

import com.devfest.wellnest.data.local.entity.ScheduleEntity

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val activeSchedule: ScheduleEntity? = null,
    val workStartHour: Int = 9,
    val workStartMinute: Int = 0,
    val workEndHour: Int = 18,
    val workEndMinute: Int = 0,
    val lunchStartHour: Int = 13,
    val lunchStartMinute: Int = 0,
    val lunchDurationMinutes: Int = 60,
    val eveningBreakHour: Int = 17,
    val eveningBreakMinute: Int = 0,
    val eveningBreakDurationMinutes: Int = 30,
    val repeatDays: String = "MON,TUE,WED,THU,FRI",
    val syncToCalendar: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)
