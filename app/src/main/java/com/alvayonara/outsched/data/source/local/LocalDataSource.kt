package com.alvayonara.outsched.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.data.source.local.room.ScheduleDao
import com.alvayonara.outsched.utils.GroupScheduleUtils.consolidateSchedule

class LocalDataSource private constructor(private val mScheduleDao: ScheduleDao) {

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(scheduleDao: ScheduleDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(scheduleDao)
            }
    }

    fun getAllUpcomingSchedules(): LiveData<List<ScheduleEntity>> = mScheduleDao.getAllUpcomingSchedules()

    fun getAllPastSchedules(): LiveData<List<ScheduleEntity>> = mScheduleDao.getAllPastSchedules()

    fun insertSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.insertSchedule(scheduleEntity)

    fun updateSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.updateSchedule(scheduleEntity)

    fun deleteSchedule(scheduleEntity: ScheduleEntity) = mScheduleDao.deleteSchedule(scheduleEntity)

    fun checkSchedule(time: Long, latitude: String, longitude: String): Boolean = mScheduleDao.checkSchedules(time, latitude, longitude)
}