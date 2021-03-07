package com.alvayonara.outsched.core.data.source.local.room

import androidx.room.*
import com.alvayonara.outsched.core.data.source.local.entity.ScheduleEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity): Completable

    @Query("UPDATE scheduleentity SET reminded = :reminded WHERE requestCode = :requestCode")
    fun updateSchedule(reminded: Boolean, requestCode: Int): Completable

    @Delete
    fun deleteSchedule(scheduleEntity: ScheduleEntity): Completable

    @Query("DELETE FROM scheduleentity WHERE requestCode = :requestCode")
    fun deleteScheduleByRequestCode(requestCode: Int): Completable

    @Query("SELECT * from scheduleentity WHERE reminded = 0 ORDER BY time ASC")
    fun getAllUpcomingSchedules(): Flowable<List<ScheduleEntity>>

    @Query("SELECT * from scheduleentity WHERE reminded = 1 ORDER BY time ASC")
    fun getAllPastSchedules(): Flowable<List<ScheduleEntity>>

    @Query("SELECT COUNT(*) FROM scheduleentity WHERE time=:time AND latitude = :latitude AND longitude = :longitude")
    fun checkSchedules(time: Long, latitude: String, longitude: String): Flowable<Int>
}