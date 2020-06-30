package com.alvayonara.outsched.utils

import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.DateItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object GroupScheduleUtils {

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

    fun consolidateSchedule(schedules: List<ScheduleEntity>): ArrayList<ScheduleListItem> {
        // Begin grouping schedule by date
        val consolidatedList = ArrayList<ScheduleListItem>()

        val groupedHashMap: HashMap<String, MutableList<ScheduleEntity>>? =
            groupDataIntoHashMap(schedules)

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
}