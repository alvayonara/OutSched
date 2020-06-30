package com.alvayonara.outsched.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity)

    @Update
    fun updateSchedule(schedule: ScheduleEntity)

    @Delete
    fun deleteSchedule(schedule: ScheduleEntity)

    @Query("SELECT * from scheduleentity WHERE reminded = 0")
    fun getAllUpcomingSchedules(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE reminded = 1")
    fun getAllPastSchedules(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE id = :id AND latitude = :latitude AND longitude = :longitude")
    fun checkSchedules(id: Int, latitude: String, longitude: String): Boolean
}