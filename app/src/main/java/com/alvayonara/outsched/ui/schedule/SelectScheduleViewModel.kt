package com.alvayonara.outsched.ui.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.api.ApiRepository
import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.remote.entity.RequestResponse
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
                ) =
                    weathersDataResults.postValue(response.body()!!.hourly.data)
            })
    }

    fun getWeathersData(): LiveData<List<ScheduleEntity>> {

        // array weather data
        val weatherList = arrayOf("Cerah", "Berawan")

        // array time data
        val timeList =
            arrayOf("07:00", "08:00", "09:00", "16:00", "17:00")


        return weathersDataResults
    }
}