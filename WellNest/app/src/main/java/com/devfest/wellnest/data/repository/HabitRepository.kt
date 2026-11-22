package com.devfest.wellnest.data.repository

import com.devfest.wellnest.data.local.dao.HabitDao
import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    fun getAllActiveHabits(): Flow<List<HabitEntity>> = habitDao.getAllActiveHabits()

    fun getTodayHabitLogs(): Flow<List<HabitLogEntity>> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return habitDao.getHabitLogsForDate(today)
    }

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)

    suspend fun logHabit(habitId: Long, completed: Boolean) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val log = HabitLogEntity(
            habitId = habitId,
            date = today,
            completed = completed,
            completedAt = if (completed) System.currentTimeMillis() else null
        )
        habitDao.insertHabitLog(log)
    }

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)
}
