package com.devfest.wellnest.domain.usecase

import com.devfest.wellnest.data.calendar.CalendarEvent
import com.devfest.wellnest.data.repository.CalendarRepository
import javax.inject.Inject

class SyncCalendarUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend fun getTodayEvents(): List<CalendarEvent> = calendarRepository.getTodayEvents()

    suspend fun createBreakEvent(title: String, startMs: Long, durationMinutes: Int): String? {
        val event = CalendarEvent(
            title = "\uD83C\uDF3F $title",
            description = "WellNest: Wellness break",
            startTimeMs = startMs,
            endTimeMs = startMs + durationMinutes * 60 * 1000L
        )
        return calendarRepository.createEvent(event)
    }
}
