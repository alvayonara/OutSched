package com.alvayonara.outsched.core.domain.usecase

import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import io.reactivex.Flowable

interface ScheduleUseCase {

    fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ): Flowable<ApiResponse<List<ScheduleListItem>>>

    fun getAllUpcomingSchedules(): Flowable<List<Schedule>>

    fun getAllPastSchedules(): Flowable<List<Schedule>>

    fun insertSchedule(schedule: Schedule)

    fun deleteSchedule(schedule: Schedule)

    fun checkSchedule(time: Long, latitude: String, longitude: String): Flowable<Int>
}