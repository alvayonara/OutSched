package com.alvayonara.outsched.core.data.source

import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.domain.repository.IScheduleRepository
import com.alvayonara.outsched.core.data.source.local.LocalDataSource
import com.alvayonara.outsched.core.data.source.remote.RemoteDataSource
import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import com.alvayonara.outsched.core.utils.DataMapper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IScheduleRepository {

    override fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ): Flowable<ApiResponse<List<ScheduleListItem>>> =
        remoteDataSource.getWeathersData(latitude, longitude, address, id, requestCode)

    override fun getAllUpcomingSchedules(): Flowable<List<Schedule>> =
        localDataSource.getAllUpcomingSchedules().map {
            DataMapper.mapScheduleListEntitiesToDomain(it)
        }

    override fun getAllPastSchedules(): Flowable<List<Schedule>> =
        localDataSource.getAllPastSchedules().map {
            DataMapper.mapScheduleListEntitiesToDomain(it)
        }

    override fun insertSchedule(schedule: Schedule) {
        localDataSource.insertSchedule(DataMapper.mapScheduleDomainToEntity(schedule))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun deleteSchedule(schedule: Schedule) {
        localDataSource.deleteSchedule(DataMapper.mapScheduleDomainToEntity(schedule))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun checkSchedule(time: Long, latitude: String, longitude: String): Flowable<Int> =
        localDataSource.checkSchedule(time, latitude, longitude)
}