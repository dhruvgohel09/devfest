package com.devfest.wellnest.data.local.dao

import androidx.room.*
import com.devfest.wellnest.data.local.entity.HabitEntity
import com.devfest.wellnest.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isActive = 1")
    fun getAllActiveHabits(): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLogEntity)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getHabitLogForDate(habitId: Long, date: String): HabitLogEntity?

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getHabitLogsForDate(date: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY date DESC LIMIT 30")
    fun getHabitLogsLast30Days(habitId: Long): Flow<List<HabitLogEntity>>
}
