package com.devfest.wellnest.di

import com.devfest.wellnest.data.calendar.CalendarDataSource
import com.devfest.wellnest.data.calendar.GoogleCalendarDataSource
import com.devfest.wellnest.data.repository.CalendarRepository
import com.devfest.wellnest.data.repository.GoalRepository
import com.devfest.wellnest.data.repository.HabitRepository
import com.devfest.wellnest.data.repository.NoteRepository
import com.devfest.wellnest.data.repository.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideScheduleRepository(
        scheduleDao: com.devfest.wellnest.data.local.dao.ScheduleDao,
        calendarDataSource: CalendarDataSource
    ): ScheduleRepository = ScheduleRepository(scheduleDao, calendarDataSource)

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: com.devfest.wellnest.data.local.dao.HabitDao
    ): HabitRepository = HabitRepository(habitDao)

    @Provides
    @Singleton
    fun provideGoalRepository(
        goalDao: com.devfest.wellnest.data.local.dao.GoalDao
    ): GoalRepository = GoalRepository(goalDao)

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: com.devfest.wellnest.data.local.dao.NoteDao
    ): NoteRepository = NoteRepository(noteDao)

    @Provides
    @Singleton
    fun provideCalendarRepository(
        calendarDataSource: CalendarDataSource
    ): CalendarRepository = CalendarRepository(calendarDataSource)
}
