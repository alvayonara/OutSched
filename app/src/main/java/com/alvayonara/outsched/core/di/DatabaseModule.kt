package com.alvayonara.outsched.core.di

import android.content.Context
import androidx.room.Room
import com.alvayonara.outsched.core.data.source.local.room.ScheduleDao
import com.alvayonara.outsched.core.data.source.local.room.ScheduleRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): ScheduleRoomDatabase = Room.databaseBuilder(
        context,
        ScheduleRoomDatabase::class.java, "Schedules.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideScheduleDao(database: ScheduleRoomDatabase): ScheduleDao = database.scheduleDao()
}