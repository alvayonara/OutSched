package com.alvayonara.outsched.data.source

import androidx.lifecycle.LiveData
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem

interface ScheduleDataSource {

    fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String
    ): LiveData<List<ScheduleListItem>>
}