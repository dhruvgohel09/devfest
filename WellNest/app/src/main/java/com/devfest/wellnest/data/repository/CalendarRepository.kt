package com.devfest.wellnest.data.repository

import com.devfest.wellnest.data.calendar.CalendarDataSource
import com.devfest.wellnest.data.calendar.CalendarEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val calendarDataSource: CalendarDataSource
) {
    suspend fun getTodayEvents(): List<CalendarEvent> = calendarDataSource.getTodayEvents()
    suspend fun createEvent(event: CalendarEvent): String? = calendarDataSource.createEvent(event)
    suspend fun updateEvent(eventId: String, event: CalendarEvent): Boolean = calendarDataSource.updateEvent(eventId, event)
    suspend fun deleteEvent(eventId: String): Boolean = calendarDataSource.deleteEvent(eventId)
}
