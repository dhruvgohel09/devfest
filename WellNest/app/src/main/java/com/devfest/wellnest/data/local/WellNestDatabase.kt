package com.devfest.wellnest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devfest.wellnest.data.local.dao.GoalDao
import com.devfest.wellnest.data.local.dao.HabitDao
import com.devfest.wellnest.data.local.dao.NoteDao
import com.devfest.wellnest.data.local.dao.ScheduleDao
import com.devfest.wellnest.data.local.entity.GoalEntity
import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.local.entity.HabitLogEntity
import com.devfest.wellnest.data.local.entity.NoteEntity
import com.devfest.wellnest.data.local.entity.ScheduleEntity
import com.devfest.wellnest.data.local.entity.StretchReminderEntity

@Database(
    entities = [
        ScheduleEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        GoalEntity::class,
        NoteEntity::class,
        StretchReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WellNestDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun habitDao(): HabitDao
    abstract fun goalDao(): GoalDao
    abstract fun noteDao(): NoteDao
}
