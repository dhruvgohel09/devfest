package com.devfest.wellnest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stretch_reminders")
data class StretchReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val days: String, // "MON,TUE,WED,THU,FRI"
    val isEnabled: Boolean = true,
    val workManagerId: String? = null
)
