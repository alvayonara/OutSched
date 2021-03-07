package com.alvayonara.outsched.core.utils

import com.alvayonara.outsched.core.domain.model.Schedule
import com.alvayonara.outsched.core.domain.model.item.DateItem
import com.alvayonara.outsched.core.domain.model.item.ScheduleItem
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.utils.ConvertUtils.dateTimeConvert
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_DATE_WITH_DAY
import com.alvayonara.outsched.core.utils.DateFormat.Companion.FORMAT_ONLY_TIME
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object GroupScheduleUtils {

    private fun groupDataIntoHashMap(listOfPojosOfJsonArray: List<Schedule>): HashMap<String, MutableList<Schedule>> {
        val groupedHashMap: HashMap<String, MutableList<Schedule>> =
            HashMap()

        for (pojoOfJsonArray in listOfPojosOfJsonArray) {
            val hashMapKey: String = pojoOfJsonArray.time.dateTimeConvert(FORMAT_DATE_WITH_DAY)
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap[hashMapKey]!!.add(pojoOfJsonArray)
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                val list: MutableList<Schedule> = ArrayList()
                list.add(pojoOfJsonArray)
                groupedHashMap[hashMapKey] = list
            }
        }

        return groupedHashMap
    }

    fun consolidateSchedule(schedules: List<Schedule>): ArrayList<ScheduleListItem> {
        // Begin grouping schedule by date
        val consolidatedList = ArrayList<ScheduleListItem>()

        val groupedHashMap: HashMap<String, MutableList<Schedule>> =
            groupDataIntoHashMap(schedules)

        // Sort hash map keys (date)
        val sortedKeys: MutableList<String> =
            ArrayList(groupedHashMap.size)
        sortedKeys.addAll(groupedHashMap.keys)

        Collections.sort(sortedKeys, object : Comparator<String> {
            // Parse key to date format as comparator
            var convertToDate: DateFormat =
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            override fun compare(o1: String, o2: String): Int {
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
                scheduleItem.schedule = pojoOfJsonArray
                consolidatedList.add(scheduleItem)
            }
        }

        return consolidatedList
    }

    fun initSchedule(
        schedules: List<Schedule>,
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ): ArrayList<Schedule> {
        val listSchedules = ArrayList<Schedule>()

        for (i in schedules.indices) {
            schedules[i].address = address
            schedules[i].latitude = latitude
            schedules[i].longitude = longitude
            schedules[i].id = id
            schedules[i].requestCode = requestCode
        }

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
                        if (schedule.time.dateTimeConvert(FORMAT_ONLY_TIME) == time) {
                            listSchedules.add(schedule)
                        }
                    }
                }
            }
        }

        return listSchedules
    }
}