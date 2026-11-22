package com.devfest.wellnest.di

import android.content.Context
import androidx.room.Room
import com.devfest.wellnest.data.local.WellNestDatabase
import com.devfest.wellnest.data.local.dao.GoalDao
import com.devfest.wellnest.data.local.dao.HabitDao
import com.devfest.wellnest.data.local.dao.NoteDao
import com.devfest.wellnest.data.local.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWellNestDatabase(@ApplicationContext context: Context): WellNestDatabase {
        return Room.databaseBuilder(
            context,
            WellNestDatabase::class.java,
            "wellnest_database"
        ).build()
    }

    @Provides
    fun provideScheduleDao(db: WellNestDatabase): ScheduleDao = db.scheduleDao()

    @Provides
    fun provideHabitDao(db: WellNestDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideGoalDao(db: WellNestDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideNoteDao(db: WellNestDatabase): NoteDao = db.noteDao()
}
