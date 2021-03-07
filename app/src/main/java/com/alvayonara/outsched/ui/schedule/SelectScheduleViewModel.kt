package com.alvayonara.outsched.ui.schedule

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.core.domain.usecase.ScheduleUseCase
import javax.inject.Inject

class SelectScheduleViewModel @Inject constructor(private val scheduleUseCase: ScheduleUseCase) : ViewModel() {

    fun getWeathers(
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ) = LiveDataReactiveStreams.fromPublisher(scheduleUseCase.getWeathersData(
        latitude, longitude, address, id, requestCode
    ))
}