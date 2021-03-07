package com.alvayonara.outsched.ui.schedule

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.usecase.ScheduleUseCase
import javax.inject.Inject

class ScheduleDetailViewModel @Inject constructor(private val scheduleUseCase: ScheduleUseCase) : ViewModel() {

    fun insert(schedule: Schedule) = scheduleUseCase.insertSchedule(schedule)

    fun delete(schedule: Schedule) = scheduleUseCase.deleteSchedule(schedule)

    fun checkScheduleData(time: Long, latitude: String, longitude: String) =
        LiveDataReactiveStreams.fromPublisher(
            scheduleUseCase.checkSchedule(
                time,
                latitude,
                longitude
            )
        )
}