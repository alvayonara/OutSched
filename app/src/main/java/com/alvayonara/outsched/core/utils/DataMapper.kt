package com.alvayonara.outsched.core.utils

import com.alvayonara.outsched.core.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.core.data.source.remote.response.ScheduleResponse
import com.alvayonara.outsched.core.domain.model.Schedule

object DataMapper {

    fun mapScheduleListResponsesToDomain(input: List<ScheduleResponse>): List<Schedule> {
        val scheduleList = ArrayList<Schedule>()
        input.map {
            val schedule = Schedule(
                time = it.time,
                summary = it.summary,
                icon = it.icon,
                temperature = it.temperature
            )
            scheduleList.add(schedule)
        }
        return scheduleList
    }

    fun mapScheduleListEntitiesToDomain(input: List<ScheduleEntity>): List<Schedule> =
        input.map {
            Schedule(
                id = it.id,
                time = it.time,
                summary = it.summary,
                icon = it.icon,
                temperature = it.temperature,
                address = it.address,
                latitude = it.latitude,
                longitude = it.longitude,
                reminded = it.reminded,
                requestCode = it.requestCode
            )
        }

    fun mapScheduleDomainToEntity(input: Schedule) = ScheduleEntity(
        id = input.id,
        time = input.time,
        summary = input.summary,
        icon = input.icon,
        temperature = input.temperature,
        address = input.address,
        latitude = input.latitude,
        longitude = input.longitude,
        reminded = input.reminded,
        requestCode = input.requestCode
    )
}