package com.devfest.wellnest.data.calendar

interface CalendarDataSource {
    suspend fun getTodayEvents(): List<CalendarEvent>
    suspend fun createEvent(event: CalendarEvent): String?
    suspend fun updateEvent(eventId: String, event: CalendarEvent): Boolean
    suspend fun deleteEvent(eventId: String): Boolean
}
