package com.alvayonara.outsched.di

import android.content.Context
import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.data.source.local.LocalDataSource
import com.alvayonara.outsched.data.source.local.room.ScheduleRoomDatabase
import com.alvayonara.outsched.data.source.remote.RemoteDataSource
import com.alvayonara.outsched.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): ScheduleRepository {

        val database = ScheduleRoomDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.scheduleDao())
        val appExecutors = AppExecutors()

        return ScheduleRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}