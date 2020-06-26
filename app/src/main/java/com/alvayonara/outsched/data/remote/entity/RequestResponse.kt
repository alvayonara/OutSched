package com.alvayonara.outsched.data.remote.entity

import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.google.gson.annotations.SerializedName

data class RequestResponse(
    @SerializedName("hourly") val hourly: HourlyResponse
)

data class HourlyResponse(
    @SerializedName("data") val data: List<ScheduleEntity>
)
