package com.devfest.wellnest.data.calendar

data class CalendarEvent(
    val id: String = "",
    val title: String,
    val description: String = "",
    val startTimeMs: Long,
    val endTimeMs: Long,
    val colorHex: String = "#4CAF82"
)
