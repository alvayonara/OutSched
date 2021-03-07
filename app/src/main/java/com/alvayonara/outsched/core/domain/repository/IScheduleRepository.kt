package com.alvayonara.outsched.core.domain.repository

import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import io.reactivex.Flowable

interface IScheduleRepository {

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