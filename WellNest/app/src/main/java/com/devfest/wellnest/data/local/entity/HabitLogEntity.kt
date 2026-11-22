package com.devfest.wellnest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String, // "yyyy-MM-dd"
    val completed: Boolean = false,
    val completedAt: Long? = null
)
