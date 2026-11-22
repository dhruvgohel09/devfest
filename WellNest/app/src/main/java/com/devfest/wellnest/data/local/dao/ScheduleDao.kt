package com.devfest.wellnest.data.local.dao

import androidx.room.*
import com.devfest.wellnest.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE isActive = 1 LIMIT 1")
    fun getActiveSchedule(): Flow<ScheduleEntity?>

    @Query("SELECT * FROM schedules")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)

    @Query("UPDATE schedules SET isActive = 0")
    suspend fun deactivateAll()
}
