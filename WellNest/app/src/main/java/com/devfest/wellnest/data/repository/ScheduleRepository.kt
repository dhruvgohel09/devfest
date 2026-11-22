package com.devfest.wellnest.data.repository

import com.devfest.wellnest.data.calendar.CalendarDataSource
import com.devfest.wellnest.data.calendar.CalendarEvent
import com.devfest.wellnest.data.local.dao.ScheduleDao
import com.devfest.wellnest.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val calendarDataSource: CalendarDataSource
) {
    fun getActiveSchedule(): Flow<ScheduleEntity?> = scheduleDao.getActiveSchedule()

    suspend fun saveSchedule(schedule: ScheduleEntity, syncToCalendar: Boolean = false): Long {
        scheduleDao.deactivateAll()
        val id = scheduleDao.insertSchedule(schedule)
        if (syncToCalendar) {
            val event = CalendarEvent(
                title = "Work Schedule",
                description = "WellNest: Work block synced",
                startTimeMs = System.currentTimeMillis(),
                endTimeMs = System.currentTimeMillis() + 8 * 60 * 60 * 1000
            )
            calendarDataSource.createEvent(event)
        }
        return id
    }

    suspend fun deleteSchedule(schedule: ScheduleEntity) = scheduleDao.deleteSchedule(schedule)
}
