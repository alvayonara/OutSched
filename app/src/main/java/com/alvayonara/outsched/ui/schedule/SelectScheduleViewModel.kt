package com.alvayonara.outsched.ui.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.api.ApiRepository
import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.remote.entity.RequestResponse
import com.alvayonara.outsched.utils.ConvertUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectScheduleViewModel : ViewModel() {

    val weathersDataResults = MutableLiveData<List<ScheduleEntity>>()

    fun setWeathersData(latitude: String, longitude: String) {
        ApiRepository().darkSky.getWeathersData(BuildConfig.DARK_SKY_KEY, latitude, longitude)
            .enqueue(object : Callback<RequestResponse> {
                override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                    Log.e("Movie Request Error: ", t.toString())
                    weathersDataResults.postValue(null)
                }

                override fun onResponse(
                    call: Call<RequestResponse>,
                    response: Response<RequestResponse>
                ) {
                    weathersDataResults.postValue(initSchedule(response.body()!!.hourly.data))
                }
            })
    }

    private fun initSchedule(schedules: List<ScheduleEntity>): ArrayList<ScheduleEntity> {
        val listSchedules = ArrayList<ScheduleEntity>()

        // array weather data
        val weatherList = arrayOf("Clear", "Cloudy")

        // array time data
        val timeList =
            arrayOf("07:00", "08:00", "09:00", "16:00", "17:00")

        for (schedule in schedules) {
            if (schedule.time * 1000 < System.currentTimeMillis()) {
                continue
            }
            for (weather in weatherList) {
                if (schedule.summary.contains(weather)) {
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

    fun getWeathersData(): LiveData<List<ScheduleEntity>> {
        return weathersDataResults
    }
}