package com.alvayonara.outsched.data.source.local

import androidx.lifecycle.LiveData
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.room.ScheduleDao

class LocalDataSource private constructor(private val mScheduleDao: ScheduleDao) {

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(scheduleDao: ScheduleDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(scheduleDao)
            }
    }

    fun getAllUpcomingSchedules(): LiveData<List<ScheduleEntity>> =
        mScheduleDao.getAllUpcomingSchedules()

    fun getAllPastSchedules(): LiveData<List<ScheduleEntity>> = mScheduleDao.getAllPastSchedules()

    fun insertSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.insertSchedule(scheduleEntity)

    fun updateSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.updateSchedule(scheduleEntity)

    fun deleteSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.deleteSchedule(scheduleEntity)
}