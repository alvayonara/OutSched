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

    @Query("DELETE FROM scheduleentity WHERE request_code = :requestCode")
    fun deleteScheduleByRequestCode(requestCode: Int)

    @Query("SELECT * from scheduleentity WHERE reminded = 0 ORDER BY time ASC")
    fun getAllUpcomingSchedules(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE reminded = 1 ORDER BY time ASC")
    fun getAllPastSchedules(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE time = :time AND latitude = :latitude AND longitude = :longitude")
    fun checkSchedules(time: Long, latitude: String, longitude: String): Boolean
}