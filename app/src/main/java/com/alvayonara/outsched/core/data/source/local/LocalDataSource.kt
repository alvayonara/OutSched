package com.alvayonara.outsched.core.data.source.local

import com.alvayonara.outsched.core.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.core.data.source.local.room.ScheduleDao
import com.alvayonara.outsched.core.domain.model.Schedule
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val mScheduleDao: ScheduleDao) {

    fun getAllUpcomingSchedules(): Flowable<List<ScheduleEntity>> =
        mScheduleDao.getAllUpcomingSchedules()

    fun getAllPastSchedules(): Flowable<List<ScheduleEntity>> = mScheduleDao.getAllPastSchedules()

    fun insertSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.insertSchedule(scheduleEntity)

    fun updateSchedule(reminded: Boolean, requestCode: Int) =
        mScheduleDao.updateSchedule(reminded, requestCode)

    fun deleteSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.deleteSchedule(scheduleEntity)

    fun checkSchedule(time: Long, latitude: String, longitude: String): Flowable<Int> =
        mScheduleDao.checkSchedules(time, latitude, longitude)
}