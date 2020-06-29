package com.alvayonara.outsched.di

import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.data.source.remote.RemoteDataSource

object Injection {

    fun provideRepository(): ScheduleRepository {

        val remoteDataSource = RemoteDataSource.getInstance()

        return ScheduleRepository.getInstance(remoteDataSource)
    }
}