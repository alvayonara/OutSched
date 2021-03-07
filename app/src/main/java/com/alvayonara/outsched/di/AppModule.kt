package com.alvayonara.outsched.di

import com.alvayonara.outsched.core.domain.usecase.ScheduleInteractor
import com.alvayonara.outsched.core.domain.usecase.ScheduleUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideScheduleUseCase(scheduleInteractor: ScheduleInteractor): ScheduleUseCase
}