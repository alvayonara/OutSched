package com.alvayonara.outsched.data.local.entity

import android.os.Parcelable
import com.alvayonara.outsched.utils.ConvertUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleEntity(
    @SerializedName("time") val time: Long,
    @SerializedName("summary") val summary: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("temperature") val temperature: Double
) : Parcelable