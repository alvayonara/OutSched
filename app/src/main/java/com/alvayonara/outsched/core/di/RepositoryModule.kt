package com.alvayonara.outsched.core.di

import com.alvayonara.outsched.core.data.source.ScheduleRepository
import com.alvayonara.outsched.core.domain.repository.IScheduleRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(scheduleRepository: ScheduleRepository): IScheduleRepository
}