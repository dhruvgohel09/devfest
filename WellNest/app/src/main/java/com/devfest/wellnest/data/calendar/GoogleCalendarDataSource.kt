package com.devfest.wellnest.data.calendar

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleCalendarDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CalendarDataSource {

    private var calendarService: Calendar? = null

    fun initialize(accountName: String) {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(CalendarScopes.CALENDAR)
        ).apply {
            selectedAccountName = accountName
        }

        calendarService = Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("WellNest")
            .build()
    }

    override suspend fun getTodayEvents(): List<CalendarEvent> {
        val service = calendarService ?: return emptyList()
        return try {
            val now = System.currentTimeMillis()
            val startOfDay = getStartOfDay(now)
            val endOfDay = getEndOfDay(now)

            val events = service.events().list("primary")
                .setTimeMin(com.google.api.client.util.DateTime(startOfDay))
                .setTimeMax(com.google.api.client.util.DateTime(endOfDay))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()

            events.items?.map { it.toCalendarEvent() } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun createEvent(event: CalendarEvent): String? {
        val service = calendarService ?: return null
        return try {
            val googleEvent = Event().apply {
                summary = event.title
                description = event.description
                start = EventDateTime().apply {
                    dateTime = com.google.api.client.util.DateTime(event.startTimeMs)
                }
                end = EventDateTime().apply {
                    dateTime = com.google.api.client.util.DateTime(event.endTimeMs)
                }
            }
            val created = service.events().insert("primary", googleEvent).execute()
            created.id
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateEvent(eventId: String, event: CalendarEvent): Boolean {
        val service = calendarService ?: return false
        return try {
            val googleEvent = service.events().get("primary", eventId).execute()
            googleEvent.summary = event.title
            googleEvent.description = event.description
            service.events().update("primary", eventId, googleEvent).execute()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteEvent(eventId: String): Boolean {
        val service = calendarService ?: return false
        return try {
            service.events().delete("primary", eventId).execute()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun Event.toCalendarEvent() = CalendarEvent(
        id = id ?: "",
        title = summary ?: "No Title",
        description = description ?: "",
        startTimeMs = start?.dateTime?.value ?: 0L,
        endTimeMs = end?.dateTime?.value ?: 0L
    )

    private fun getStartOfDay(timeMs: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = timeMs
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(timeMs: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = timeMs
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
        cal.set(java.util.Calendar.MINUTE, 59)
        cal.set(java.util.Calendar.SECOND, 59)
        return cal.timeInMillis
    }
}
