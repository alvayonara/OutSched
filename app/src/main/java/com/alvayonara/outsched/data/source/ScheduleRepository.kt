package com.alvayonara.outsched.data.source

import androidx.lifecycle.LiveData
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.data.source.remote.RemoteDataSource

class ScheduleRepository private constructor(private val remoteDataSource: RemoteDataSource) :
    ScheduleDataSource {

    companion object {
        @Volatile
        private var instance: ScheduleRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): ScheduleRepository =
            instance ?: synchronized(this) {
                instance ?: ScheduleRepository(remoteDataSource)
            }
    }

    override fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String
    ): LiveData<List<ScheduleListItem>> =
        remoteDataSource.getWeathersData(latitude, longitude, address)
}