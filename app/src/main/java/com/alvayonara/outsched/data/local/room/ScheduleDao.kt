package com.alvayonara.outsched.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alvayonara.outsched.data.local.entity.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: ScheduleEntity)

    @Update
    fun update(schedule: ScheduleEntity)

    @Delete
    fun delete(schedule: ScheduleEntity)

    @Query("SELECT * from scheduleentity WHERE reminded = 0")
    fun getAllUpcomingSchedule(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE reminded = 1")
    fun getAllPastSchedule(): LiveData<List<ScheduleEntity>>
}