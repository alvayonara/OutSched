package com.alvayonara.outsched.ui.dashboard

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.core.domain.usecase.ScheduleUseCase
import javax.inject.Inject

class DashboardViewModel @Inject constructor(scheduleUseCase: ScheduleUseCase) : ViewModel() {

    val getUpcomingSchedules = LiveDataReactiveStreams.fromPublisher(scheduleUseCase.getAllUpcomingSchedules())

    val getPastSchedules = LiveDataReactiveStreams.fromPublisher(scheduleUseCase.getAllPastSchedules())
}