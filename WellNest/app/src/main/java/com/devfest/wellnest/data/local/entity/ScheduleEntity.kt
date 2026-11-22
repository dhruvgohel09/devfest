package com.devfest.wellnest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workStartHour: Int,
    val workStartMinute: Int,
    val workEndHour: Int,
    val workEndMinute: Int,
    val lunchStartHour: Int,
    val lunchStartMinute: Int,
    val lunchDurationMinutes: Int,
    val eveningBreakHour: Int,
    val eveningBreakMinute: Int,
    val eveningBreakDurationMinutes: Int,
    val repeatDays: String, // "MON,TUE,WED,THU,FRI"
    val calendarEventId: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
