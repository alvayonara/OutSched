package com.alvayonara.outsched.core.data.source.remote.response

import com.squareup.moshi.Json

data class RequestResponse(
    @field:Json(name = "hourly") val hourly: HourlyResponse
)

data class HourlyResponse(
    @field:Json(name = "data") val data: List<ScheduleResponse>
)

data class ScheduleResponse(
    @field:Json(name = "time") var time: Long,
    @field:Json(name = "summary") var summary: String,
    @field:Json(name = "icon") var icon: String,
    @field:Json(name = "temperature") var temperature: Double,
)
