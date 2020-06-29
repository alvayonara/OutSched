package com.alvayonara.outsched.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.api.ApiRepository
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.DateItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.data.source.remote.response.RequestResponse
import com.alvayonara.outsched.utils.ConvertUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
                        initSchedule(
                            response.body()!!.hourly.data,
                            latitude,
                            longitude,
                            address
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

    ): ArrayList<ScheduleListItem> {
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

        // Begin grouping schedule by date
        val consolidatedList = ArrayList<ScheduleListItem>()

        val groupedHashMap: HashMap<String, MutableList<ScheduleEntity>>? =
            groupDataIntoHashMap(listSchedules)

        // Sort hash map keys (date)
        val sortedKeys: MutableList<String> =
            ArrayList(groupedHashMap!!.size)
        sortedKeys.addAll(groupedHashMap.keys)

        Collections.sort(sortedKeys, object : Comparator<String?> {
            // Parse key to date format as comparator
            var convertToDate: DateFormat =
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            override fun compare(o1: String?, o2: String?): Int {
                return try {
                    convertToDate.parse(o1).compareTo(convertToDate.parse(o2))
                } catch (e: ParseException) {
                    throw IllegalArgumentException(e)
                }
            }
        })

        // Loop for each from sort key result
        for (date in sortedKeys) {
            val dateItem =
                DateItem()
            dateItem.dateList = date
            consolidatedList.add(dateItem)

            for (pojoOfJsonArray in groupedHashMap[date]!!) {
                val scheduleItem =
                    ScheduleItem()
                scheduleItem.scheduleEntity = pojoOfJsonArray
                consolidatedList.add(scheduleItem)
            }
        }

        return consolidatedList
    }

    private fun groupDataIntoHashMap(listOfPojosOfJsonArray: List<ScheduleEntity>): HashMap<String, MutableList<ScheduleEntity>>? {
        val groupedHashMap: HashMap<String, MutableList<ScheduleEntity>> =
            HashMap()

        for (pojoOfJsonArray in listOfPojosOfJsonArray) {
            val hashMapKey: String = ConvertUtils.convertTimeToDate(pojoOfJsonArray.time)!!
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap[hashMapKey]!!.add(pojoOfJsonArray)
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                val list: MutableList<ScheduleEntity> = ArrayList()
                list.add(pojoOfJsonArray)
                groupedHashMap[hashMapKey] = list
            }
        }

        return groupedHashMap
    }
}