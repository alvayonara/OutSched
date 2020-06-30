package com.alvayonara.outsched.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.api.ApiRepository
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.data.source.remote.response.RequestResponse
import com.alvayonara.outsched.utils.ConvertUtils
import com.alvayonara.outsched.utils.GroupScheduleUtils.consolidateSchedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance
                ?: synchronized(this) {
                    instance
                        ?: RemoteDataSource()
                }
    }

    fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String
    ): LiveData<List<ScheduleListItem>> {
        val weathersDataResults = MutableLiveData<List<ScheduleListItem>>()

        ApiRepository().darkSky.getWeathersData(
            BuildConfig.DARK_SKY_KEY,
            latitude,
            longitude
        )
            .enqueue(object : Callback<RequestResponse> {
                override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                    Log.e("Weather Request Error: ", t.toString())

                    weathersDataResults.postValue(null)
                }

                override fun onResponse(
                    call: Call<RequestResponse>,
                    response: Response<RequestResponse>
                ) =
                    weathersDataResults.postValue(
                        consolidateSchedule(
                            initSchedule(
                                response.body()!!.hourly.data,
                                latitude,
                                longitude,
                                address
                            )
                        )
                    )
            })

        return weathersDataResults
    }

    private fun initSchedule(
        schedules: List<ScheduleEntity>,
        latitude: String,
        longitude: String,
        address: String

    ): ArrayList<ScheduleEntity> {
        val listSchedules = ArrayList<ScheduleEntity>()

        for (i in schedules.indices) {
            schedules[i].address = address
            schedules[i].latitude = latitude
            schedules[i].longitude = longitude
        }

        // array weather data
        val weatherList = arrayOf("Clear", "Cloudy")

        // array time data
        val timeList =
            arrayOf("07:00", "08:00", "09:00", "16:00", "17:00")

        for (schedule in schedules) {
            if (schedule.time!! * 1000 < System.currentTimeMillis()) {
                continue
            }
            for (weather in weatherList) {
                if (schedule.summary!!.contains(weather)) {
                    for (time in timeList) {
                        if (ConvertUtils.convertTimeToHour(schedule.time).equals(time)) {
                            listSchedules.add(schedule)
                        }
                    }
                }
            }
        }

        return listSchedules
    }
}