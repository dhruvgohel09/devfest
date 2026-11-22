package com.devfest.wellnest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String, // HYDRATION, SLEEP, FITNESS, MINDFULNESS, NUTRITION
    val targetValue: Float,
    val currentValue: Float = 0f,
    val unit: String, // "glasses", "hours", "steps", "minutes"
    val deadline: Long? = null,
    val calendarEventId: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
