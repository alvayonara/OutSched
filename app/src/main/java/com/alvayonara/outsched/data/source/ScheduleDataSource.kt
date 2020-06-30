package com.alvayonara.outsched.data.source

import androidx.lifecycle.LiveData
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem

interface ScheduleDataSource {

    fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String
    ): LiveData<List<ScheduleListItem>>

    fun getAllUpcomingSchedules(): LiveData<List<ScheduleEntity>>

    fun getAllPastSchedules(): LiveData<List<ScheduleEntity>>

    fun insertSchedule(scheduleEntity: ScheduleEntity)

    fun updateSchedule(scheduleEntity: ScheduleEntity)

    fun deleteSchedule(scheduleEntity: ScheduleEntity)

    fun checkSchedule(id: Int, latitude: String, longitude: String): LiveData<Boolean>
}