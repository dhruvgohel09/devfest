package com.devfest.wellnest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // HYDRATION, SLEEP, FITNESS, MINDFULNESS
    val frequency: String, // DAILY, WEEKDAYS, CUSTOM
    val streak: Int = 0,
    val targetCount: Int = 1,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
