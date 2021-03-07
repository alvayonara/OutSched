package com.alvayonara.outsched.core.domain.usecase

import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.domain.repository.IScheduleRepository
import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import io.reactivex.Flowable
import javax.inject.Inject

class ScheduleInteractor @Inject constructor(private val scheduleRepository: IScheduleRepository) :
    ScheduleUseCase {

    override fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ): Flowable<ApiResponse<List<ScheduleListItem>>> =
        scheduleRepository.getWeathersData(latitude, longitude, address, id, requestCode)

    override fun getAllUpcomingSchedules(): Flowable<List<Schedule>> =
        scheduleRepository.getAllUpcomingSchedules()

    override fun getAllPastSchedules(): Flowable<List<Schedule>> =
        scheduleRepository.getAllPastSchedules()

    override fun insertSchedule(schedule: Schedule) = scheduleRepository.insertSchedule(schedule)

    override fun deleteSchedule(schedule: Schedule) = scheduleRepository.deleteSchedule(schedule)

    override fun checkSchedule(time: Long, latitude: String, longitude: String): Flowable<Int> =
        scheduleRepository.checkSchedule(time, latitude, longitude)
}