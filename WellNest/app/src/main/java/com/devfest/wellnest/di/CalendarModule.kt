package com.devfest.wellnest.di

import com.devfest.wellnest.data.calendar.CalendarDataSource
import com.devfest.wellnest.data.calendar.GoogleCalendarDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    @Binds
    @Singleton
    abstract fun bindCalendarDataSource(
        googleCalendarDataSource: GoogleCalendarDataSource
    ): CalendarDataSource
}
